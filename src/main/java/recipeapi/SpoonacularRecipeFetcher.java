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
    public List<Recipe> getRecipesByIngredients(List<String> ingredients, int number, int ranking, boolean ignorePantry) throws IngredientNotFoundException {
        return List.of();
    }

    @Override
    public Recipe getRecipeInfo(int id, boolean includeNutrition, boolean addWinePairing, boolean addTasteData) throws RecipeNotFoundException {
        return null;
    }

    @Override
    public Recipe getRecipeInstructions(int id, boolean steBreakdown) throws RecipeNotFoundException {
        return null;
    }

    @Override
    public Recipe getNutrition(int id) throws RecipeNotFoundException {
        return null;
    }
}