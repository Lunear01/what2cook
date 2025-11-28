package recipeapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Ingredient;
import entity.Recipe;
import okhttp3.HttpUrl;
import recipeapi.exceptions.IngredientNotFoundException;
import recipeapi.exceptions.RecipeNotFoundException;

public class SpoonacularRecipeFetcher implements RecipeFetcher {

    private static final String API_KEY = System.getenv().getOrDefault(
            "SPOONACULAR_API_KEY",
            "4932d6f2116641318a14ddc9f10fd899"
    );

    private static final String BASE_URL = "https://api.spoonacular.com/recipes";
    private final HttpService httpService;

    // Constructor for HttpService
    public SpoonacularRecipeFetcher() {
        this.httpService = new HttpService();
    }

    // Get Recipes by Ingredients
    @Override
    public List<Recipe> getRecipesByIngredients(List<String> ingredients,
                                                int number,
                                                int ranking,
                                                boolean ignorePantry)
            throws IngredientNotFoundException {

        // Construct request URL
        final HttpUrl url = HttpUrl.parse(BASE_URL + "/findByIngredients").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .addQueryParameter("ingredients", String.join(",", ingredients))
                .addQueryParameter("number", String.valueOf(number))
                .addQueryParameter("ranking", String.valueOf(ranking))
                .addQueryParameter("ignorePantry", String.valueOf(ignorePantry))
                .build();

        try {
            final String json = httpService.get(url);
            final JSONArray array = parseResponse(ingredients, json);

            if (array.isEmpty()) {
                throw new IngredientNotFoundException(
                        "No recipes found for ingredients: " + ingredients);
            }

            final List<Recipe> results = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);

                // ----- Build basic recipe (id, title, image) -----
                Recipe recipe = Recipe.builder()
                        .setId(obj.getInt("id"))
                        .setTitle(obj.getString("title"))
                        .setImage(obj.optString("image", null))
                        .build();

                // ----- Extract ingredient names -----
                final List<Ingredient> ingList = new ArrayList<>();
                final JSONArray used = obj.getJSONArray("usedIngredients");
                final JSONArray missed = obj.getJSONArray("missedIngredients");

                for (int u = 0; u < used.length(); u++) {
                    final JSONObject ingObj = used.getJSONObject(u);
                    final String ingredientName = ingObj.getString("name");
                    final int ingredientId = ingObj.optInt("id", -1);

                    ingList.add(
                            Ingredient.builder()
                                    .setName(ingredientName)
                                    .setId(ingredientId)
                                    .build()
                    );
                }

                for (int m = 0; m < missed.length(); m++) {
                    final JSONObject ingObj = missed.getJSONObject(m);
                    final String ingredientName = ingObj.getString("name");
                    final int ingredientId = ingObj.optInt("id", -1);

                    ingList.add(
                            Ingredient.builder()
                                    .setName(ingredientName)
                                    .setId(ingredientId)
                                    .build()
                    );
                }

                // 用 toBuilder 给 recipe 补上 ingredientNames
                recipe = recipe.toBuilder()
                        .setIngredientNames(ingList)
                        .build();

                results.add(recipe);
            }

            return results;

        } catch (IOException error) {
            throw new RuntimeException("Failed to fetch recipes: " + error.getMessage(), error);
        }
    }

    // 2. Get recipe info
    @Override
    public Recipe getRecipeInfo(int id,
                                boolean includeNutrition,
                                boolean addWinePairing,
                                boolean addTasteData)
            throws RecipeNotFoundException {

        // Construct request URL
        final HttpUrl url = HttpUrl.parse(BASE_URL + "/" + id + "/information").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .addQueryParameter("includeNutrition", String.valueOf(includeNutrition))
                .addQueryParameter("addWinePairing", String.valueOf(addWinePairing))
                .addQueryParameter("addTasteData", String.valueOf(addTasteData))
                .build();

        try {
            final String json = httpService.get(url);
            final JSONObject obj = new JSONObject(json);

            // ----- Ingredient names -----
            final List<Ingredient> ingredients = new ArrayList<>();
            final JSONArray ingArray = obj.getJSONArray("extendedIngredients");

            for (int i = 0; i < ingArray.length(); i++) {
                final JSONObject ingObj = ingArray.getJSONObject(i);
                final String ingredientName = ingObj.getString("name");
                final int ingredientId = ingObj.optInt("id", -1);

                ingredients.add(
                        Ingredient.builder()
                                .setName(ingredientName)
                                .setId(ingredientId)
                                .build()
                );
            }

            // ----- 用 Builder 一次性把 Recipe 所有字段设好 -----
            return Recipe.builder()
                    .setId(id)
                    .setTitle(obj.getString("title"))
                    .setImage(obj.optString("image", null))
                    .setIngredientNames(ingredients)
                    .setHealthScore(obj.optInt("healthScore", -1))
                    .setCalories(extractCalories(obj, includeNutrition))
                    .setInstructions(obj.optString("instructions", null))
                    .build();

        } catch (IOException error) {
            throw new RecipeNotFoundException("Error retrieving recipe info: " + error.getMessage());
        }
    }

    // Get recipe instructions
    @Override
    public Recipe getRecipeInstructions(int id, boolean stepBreakdown)
            throws RecipeNotFoundException {

        // Construct request URL
        final HttpUrl url = HttpUrl.parse(BASE_URL + "/" + id + "/analyzedInstructions").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .addQueryParameter("stepBreakdown", String.valueOf(stepBreakdown))
                .build();

        try {
            final String json = httpService.get(url);
            final JSONArray array = new JSONArray(json);

            // No instructions available
            if (array.isEmpty()) {
                return Recipe.builder()
                        .setId(id)
                        .setInstructions("No instructions available.")
                        .build();
            }

            // ----- Build instructions -----
            final JSONObject instructionBlock = array.getJSONObject(0);
            final JSONArray steps = instructionBlock.getJSONArray("steps");

            final StringBuilder sb = new StringBuilder();

            for (int i = 0; i < steps.length(); i++) {
                final JSONObject step = steps.getJSONObject(i);
                final int number = step.optInt("number", i + 1);
                final String text = step.getString("step");

                if (stepBreakdown) {
                    sb.append(number).append(". ").append(wrapAt60(text)).append("\n");
                } else {
                    sb.append(wrapAt60(text)).append("\n");
                }
            }

            // ----- Return recipe with computed instructions -----
            return Recipe.builder()
                    .setId(id)
                    .setInstructions(sb.toString().trim())
                    .build();

        } catch (IOException error) {
            throw new RecipeNotFoundException("Error retrieving instructions: " + error.getMessage());
        }
    }

    // Helper: Parse JSON response
    private static JSONArray parseResponse(List<String> ingredients, String json)
            throws IngredientNotFoundException, IOException {

        // Spoonacular errors returns JSON with status = "failure"
        if (json.trim().startsWith("{")) {
            final JSONObject obj = new JSONObject(json);
            if (obj.optString("status").equals("failure")) {
                throw new IOException("Spoonacular error: " + obj.optString("message"));
            }
        }

        return new JSONArray(json);
    }

    private int extractCalories(JSONObject obj, boolean includeNutrition) {

        int result = -1;

        if (includeNutrition && obj.has("nutrition")) {

            final JSONObject nutrition = obj.getJSONObject("nutrition");

            if (nutrition.has("nutrients")) {

                final JSONArray nutrients = nutrition.getJSONArray("nutrients");

                for (int i = 0; i < nutrients.length(); i++) {
                    final JSONObject nut = nutrients.getJSONObject(i);
                    if ("Calories".equalsIgnoreCase(nut.getString("name"))) {
                        result = (int) Math.round(nut.getDouble("amount"));
                        break;
                    }
                }
            }
        }

        return result;
    }

    // Helper: instruction parsing, capping each line at 60 characters
    private String wrapAt60(String text) {
        final StringBuilder wrapped = new StringBuilder();
        int lineLength = 0;

        for (String word : text.split(" ")) {
            final int maxLength = 60;
            if (lineLength + word.length() + 1 > maxLength) {
                wrapped.append("\n");
                lineLength = 0;
            }
            wrapped.append(word).append(" ");
            lineLength += word.length() + 1;
        }

        return wrapped.toString().trim();
    }
}
