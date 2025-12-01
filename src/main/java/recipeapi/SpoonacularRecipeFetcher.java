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
            "a8caa3ad56aa4b7ba4a935fda8cfabdd"
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

        // Execute request and parse response
        try {
            final String json = httpService.get(url);
            final JSONArray array = parseResponse(ingredients, json);

            if (array.isEmpty()) {
                throw new IngredientNotFoundException("No recipes found for ingredients: "
                        + String.join(",", ingredients));
            }

            final List<Recipe> results = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);

                // Extract and construct Ingredient Objects using helper method
                final List<Ingredient> ingList = extractIngredients(obj);

                final Recipe recipe = Recipe.builder()
                        .setId(obj.getInt("id"))
                        .setTitle(obj.getString("title"))
                        .setImage(obj.getString("image"))
                        .setIngredientNames(ingList)
                        .build();

                results.add(recipe);
            }

            return results;

        }
        catch (IOException error) {
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

        // Execute request and parse response
        try {
            final String json = httpService.get(url);
            final JSONObject obj = new JSONObject(json);

            // Ingredient names
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

            // Use builder to construct Recipe Objects
            return Recipe.builder()
                    .setId(id)
                    .setTitle(obj.getString("title"))
                    .setIngredientNames(ingredients)
                    .setHealthScore(obj.optInt("healthScore", -1))
                    .setCalories(extractCalories(obj, includeNutrition))
                    .build();
        }
        catch (IOException error) {
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

        // Execute request and parse response
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

            // Build instructions
            final JSONObject instructionBlock = array.getJSONObject(0);
            final JSONArray steps = instructionBlock.getJSONArray("steps");

            final StringBuilder sb = new StringBuilder();

            for (int i = 0; i < steps.length(); i++) {
                final JSONObject step = steps.getJSONObject(i);
                final int number = step.optInt("number", i + 1);
                final String text = step.getString("step");

                // Always apply a new line after every 60 characters wrapAt60 () for GUI formatting
                if (stepBreakdown) {
                    sb.append(number).append(". ").append(wrapAt60(text)).append("\n");
                }
                else {
                    sb.append(wrapAt60(text)).append("\n");
                }
            }

            // Return recipe with computed instructions
            return Recipe.builder()
                    .setId(id)
                    .setInstructions(sb.toString().trim())
                    .build();

        }
        catch (IOException error) {
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

    // Helper: Extract all ingredients need for recipes.
    private List<Ingredient> extractIngredients(JSONObject recipeObj) {
        final List<Ingredient> ingredients = new ArrayList<>();

        // Process used ingredients
        final JSONArray used = recipeObj.getJSONArray("usedIngredients");
        for (int i = 0; i < used.length(); i++) {
            final JSONObject ingObj = used.getJSONObject(i);
            ingredients.add(createIngredient(ingObj));
        }

        // Process missed ingredients
        final JSONArray missed = recipeObj.getJSONArray("missedIngredients");
        for (int i = 0; i < missed.length(); i++) {
            final JSONObject ingObj = missed.getJSONObject(i);
            ingredients.add(createIngredient(ingObj));
        }

        return ingredients;
    }

    // Helper: Create Ingredient objects
    private Ingredient createIngredient(JSONObject ingObj) {
        final String name = ingObj.getString("name");
        final int id = ingObj.optInt("id", -1);

        return Ingredient.builder()
                .setName(name)
                .setId(id)
                .build();
    }

    // Helper: Process calorie information for recipes
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
