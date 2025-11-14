package recipeapi;

import entity.Recipe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class SpoonacularRecipeFetcher implements RecipeFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_KEY = "a8caa3ad56aa4b7ba4a935fda8cfabdd"; // Spoonacular API key
    private static final String BASE_URL = "https://api.spoonacular.com/recipes";

    /**
     * Fetch recipes by ingredients using the Spoonacular API.
     * @param ingredients list of ingredients
     * @param number maximum number of recipes to return
     * @param ranking int, whether to maximize used ingredients (1) or minimize missing ingredients (2) first
     * @param ignorePantry boolean, whether to ignore typical pantry items
     * @return list of Recipe objects
     * @throws IngredientNotFoundException if ingredients are not found
     */
    @Override
    public List<Recipe> getRecipesByIngredients(List<String> ingredients, int number, int ranking,
                                                boolean ignorePantry) throws IngredientNotFoundException {
        List<Recipe> recipes = new ArrayList<>();

        try {
            String ingredientQuery = String.join(",", ingredients);

            // Build the request URL
            String rankingValue = (ranking == 1) ? "maximize" : "minimize";
            String url = String.format("%s/findByIngredients?apiKey=%s&ingredients=%s&number=%d&ranking=%d&ignorePantry=%b",
                    BASE_URL, API_KEY, ingredientQuery, number, ranking, ignorePantry);

            // Create the request
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            // Execute the request
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new IngredientNotFoundException("Failed to fetch recipes: " + response.code());
            }

            // Parse the response
            assert response.body() != null;
            String responseBody = response.body().string();
            JSONArray jsonArray = new JSONArray(responseBody);

            // Convert JSON to Recipe objects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeJson = jsonArray.getJSONObject(i);
                Recipe recipe = parseRecipeFromJson(recipeJson);
                recipes.add(recipe);
            }

            response.close();

        } catch (IOException e) {
            throw new IngredientNotFoundException("Error fetching recipes: " + e.getMessage());
        }

        return recipes;
    }

    /**
     * Parse a JSON object into a Recipe object.
     * @param recipeJson the JSON object containing recipe data
     * @return a Recipe object
     */
    private Recipe parseRecipeFromJson(JSONObject recipeJson) {
        int id = recipeJson.getInt("id");
        String title = recipeJson.getString("title");
        String image = recipeJson.getString("image");

        // Parse used ingredients
        List<String> usedIngredients = new ArrayList<>();
        JSONArray usedIngredientsArray = recipeJson.getJSONArray("usedIngredients");
        for (int i = 0; i < usedIngredientsArray.length(); i++) {
            JSONObject ingredient = usedIngredientsArray.getJSONObject(i);
            usedIngredients.add(ingredient.getString("name"));
        }
        int usedIngredientCount = usedIngredients.size();

        // Parse missed ingredients
        List<String> missedIngredients = new ArrayList<>();
        JSONArray missedIngredientsArray = recipeJson.getJSONArray("missedIngredients");
        for (int i = 0; i < missedIngredientsArray.length(); i++) {
            JSONObject ingredient = missedIngredientsArray.getJSONObject(i);
            missedIngredients.add(ingredient.getString("name"));
        }
        int missedIngredientCount = missedIngredients.size();

        return new Recipe(id, title, usedIngredients, missedIngredients,
                usedIngredientCount, missedIngredientCount, image);
    }
}
