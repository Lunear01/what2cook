package recipeapi;

import entity.Recipe;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpoonacularRecipeFetcher implements RecipeFetcher {

    private static final OkHttpClient client = new OkHttpClient();

    private static final String API_KEY = System.getenv().getOrDefault(
            "SPOONACULAR_API_KEY",
            "a8caa3ad56aa4b7ba4a935fda8cfabdd"
    );

    private static final String BASE_URL = "https://api.spoonacular.com/recipes";


    // =====================================================================================
    // 1. GET RECIPES BY INGREDIENTS
    // =====================================================================================
    @Override
    public List<Recipe> getRecipesByIngredients(List<String> ingredients,
                                                int number,
                                                int ranking,
                                                boolean ignorePantry)
            throws IngredientNotFoundException {


        // Construct request URL
        HttpUrl url = HttpUrl.parse(BASE_URL + "/findByIngredients").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .addQueryParameter("ingredients", String.join(",", ingredients))
                .addQueryParameter("number", String.valueOf(number))
                .addQueryParameter("ranking", String.valueOf(ranking))
                .addQueryParameter("ignorePantry", String.valueOf(ignorePantry))
                .build();

        // Construct request
        Request request = new Request.Builder().url(url).build();


        // Execute request and parse response
        try (Response response = client.newCall(request).execute()) {

            JSONArray array = parseResponse(ingredients, response);

            if (array.isEmpty()) {
                throw new IngredientNotFoundException("No recipes found for ingredients: " + ingredients);
            }

            List<Recipe> results = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                Recipe recipe = new Recipe();
                recipe.setId(obj.getInt("id"));
                recipe.setTitle(obj.getString("title"));

                // Extract ingredient names
                List<String> ingNames = new ArrayList<>();
                JSONArray used = obj.getJSONArray("usedIngredients");
                JSONArray missed = obj.getJSONArray("missedIngredients");

                for (int u = 0; u < used.length(); u++) {
                    ingNames.add(used.getJSONObject(u).getString("name"));
                }
                for (int m = 0; m < missed.length(); m++) {
                    ingNames.add(missed.getJSONObject(m).getString("name"));
                }

                recipe.setIngredientNames(ingNames);

                results.add(recipe);
            }

            return results;

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch recipes: " + e.getMessage(), e);
        }
    }


    // =====================================================================================
    // 2. GET RECIPE INFORMATION
    // =====================================================================================
    @Override
    public Recipe getRecipeInfo(int id,
                                boolean includeNutrition,
                                boolean addWinePairing,
                                boolean addTasteData)
            throws RecipeNotFoundException {

        // Construct request URL
        HttpUrl url = HttpUrl.parse(BASE_URL + "/" + id + "/information").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .addQueryParameter("includeNutrition", String.valueOf(includeNutrition))
                .addQueryParameter("addWinePairing", String.valueOf(addWinePairing))
                .addQueryParameter("addTasteData", String.valueOf(addTasteData))
                .build();

        // Construct request
        Request request = new Request.Builder().url(url).build();

        // Execute request and parse response
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RecipeNotFoundException("Recipe with ID " + id + " not found");
            }

            String json = response.body().string();
            JSONObject obj = new JSONObject(json);

            Recipe recipe = new Recipe();
            recipe.setId(id);
            recipe.setTitle(obj.getString("title"));

            // Ingredient names
            List<String> ingredients = new ArrayList<>();
            JSONArray ingArray = obj.getJSONArray("extendedIngredients");
            for (int i = 0; i < ingArray.length(); i++) {
                ingredients.add(ingArray.getJSONObject(i).getString("name"));
            }
            recipe.setIngredientNames(ingredients);

            // Health score
            recipe.setHealthScore(obj.optInt("healthScore", 0));

            // Calories if included
            if (includeNutrition && obj.has("nutrition")) {
                JSONObject nutrition = obj.getJSONObject("nutrition");
                JSONArray nutrients = nutrition.getJSONArray("nutrients");
                for (int i = 0; i < nutrients.length(); i++) {
                    JSONObject n = nutrients.getJSONObject(i);
                    if (n.getString("name").equalsIgnoreCase("Calories")) {
                        recipe.setCalories((int) n.getDouble("amount"));
                    }
                }
            }

            return recipe;

        } catch (IOException e) {
            throw new RecipeNotFoundException("Error retrieving recipe info: " + e.getMessage());
        }
    }


    // =====================================================================================
    // 3. GET RECIPE INSTRUCTIONS
    // =====================================================================================
    @Override
    public Recipe getRecipeInstructions(int id, boolean stepBreakdown)
            throws RecipeNotFoundException {

        //Construct request URL
        HttpUrl url = HttpUrl.parse(BASE_URL + "/" + id + "/analyzedInstructions").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build();

        //Construct request
        Request request = new Request.Builder().url(url).build();

        //Execute request and parse response
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RecipeNotFoundException("Recipe instructions for ID " + id + " not found");
            }

            assert response.body() != null;
            String json = response.body().string();
            JSONArray array = new JSONArray(json);

            Recipe recipe = new Recipe();
            recipe.setId(id);

            // No instructions available
            if (array.isEmpty()) {
                recipe.setInstructions("No instructions available.");
                return recipe;
            }

            JSONObject instructionBlock = array.getJSONObject(0);
            JSONArray steps = instructionBlock.getJSONArray("steps");

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                int number = step.optInt("number", i + 1);
                String text = step.getString("step");

                if (stepBreakdown) {
                    sb.append(number).append(". ").append(text).append("\n");
                } else {
                    sb.append(text).append(" ");
                }
            }

            recipe.setInstructions(sb.toString().trim());
            return recipe;

        } catch (IOException e) {
            throw new RecipeNotFoundException("Error retrieving instructions: " + e.getMessage());
        }
    }


    // =====================================================================================
    // 4. GET NUTRITION
    // =====================================================================================
    @Override
    public Recipe getNutrition(int id) throws RecipeNotFoundException {

        // Construct request URL
        HttpUrl url = HttpUrl.parse(BASE_URL + "/" + id + "/nutritionWidget.json").newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build();

        // Construct request
        Request request = new Request.Builder().url(url).build();

        // Execute request and parse response
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RecipeNotFoundException("Nutrition for recipe ID " + id + " not found");
            }

            assert response.body() != null;
            String json = response.body().string();
            JSONObject obj = new JSONObject(json);

            Recipe recipe = new Recipe();
            recipe.setId(id);

            // Calorie field
            recipe.setCalories(obj.optInt("calories", 0));

            return recipe;

        } catch (IOException e) {
            throw new RecipeNotFoundException("Error retrieving nutrition: " + e.getMessage());
        }
    }


    // =====================================================================================
    // RESPONSE PARSER
    // =====================================================================================
    @NotNull
    private static JSONArray parseResponse(List<String> ingredients, Response response)
            throws IngredientNotFoundException, IOException {

        if (!response.isSuccessful()) {
            if (response.code() == 404) {
                throw new IngredientNotFoundException("Ingredients not found: " + ingredients);
            }
            throw new IOException("HTTP error " + response.code() + ": " + response.message());
        }

        assert response.body() != null;
        String json = response.body().string();

        // Spoonacular errors sometimes return JSON with status=failure
        if (json.trim().startsWith("{")) {
            JSONObject obj = new JSONObject(json);
            if (obj.optString("status").equals("failure")) {
                throw new IOException("Spoonacular error: " + obj.optString("message"));
            }
        }

        return new JSONArray(json);
    }
}