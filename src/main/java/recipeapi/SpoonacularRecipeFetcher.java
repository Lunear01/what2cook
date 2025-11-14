package recipeapi;

import entity.Recipe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


/**
 * Fetch recipes by ingredients using the Spoonacular API.
 * @param ingredients list of ingredients
 * @param number maximum number of recipes to return
 * @param ranking int, whether to maximize used ingredients (1) or minimize missing ingredients (2) first
 * @param ignorePantry boolean, whether to ignore typical pantry items
 * @return list of Recipe objects
 * @throws IngredientNotFoundException if ingredients are not found
 */

public class SpoonacularRecipeFetcher implements RecipeFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_KEY = "a8caa3ad56aa4b7ba4a935fda8cfabdd";
    private static final String BASE_URL = "https://api.spoonacular.com/recipes";

    @Override
    public List<Recipe> getRecipesByIngredients(List<String> ingredients, int number, int ranking,
                                                boolean ignorePantry) throws IngredientNotFoundException {

        final String url = BASE_URL + "findByIngredients" + "?apiKey=" + API_KEY + "&ingredients="
                + String.join(",", ingredients) + "&number=" + number + "&ranking=" + ranking
                + "&ignorePantry=" + ignorePantry;

        Request request = new Request.Builder()
                .url(url)
                .build();

        List<Recipe> recipeList;

        try {
            // Get Response from the client
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            // Get string body of the response and Parse into JSON
            String responseBody = response.body().string();
            JSONObject Json_data = new JSONObject(responseBody);

            recipeList = new ArrayList<>();
            JSONArray recipeInfo = new JSONArray(Json_data.getJSONArray("message"));

            for (int i = 0; i < recipeInfo.length(); i++) {
                    String recipeInformation = recipeInfo.getString(i);
                    recipeList.add(recipeInformation);

        }
        catch (IOException e) {
            throw new IngredientNotFoundException("Ingredient not found");
        }

        return recipeList;
    }

    @Override
    public Recipe getRecipeInfo(int id, boolean includeNutrition, boolean addWinePairing,
                                boolean addTasteData) throws RecipeNotFoundException {
        return null;
    }

    @Override
    public Recipe getRecipeInstructions(int id, boolean stepBreakdown) throws RecipeNotFoundException {
        return null;
    }

    @Override
    public Recipe getNutrition(int id) throws RecipeNotFoundException {
        return null;
    }
}