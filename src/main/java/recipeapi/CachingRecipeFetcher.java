package recipeapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Ingredient;
import entity.Recipe;
import recipeapi.exceptions.IngredientNotFoundException;
import recipeapi.exceptions.RecipeNotFoundException;

public class CachingRecipeFetcher implements RecipeFetcher {
    private static final String KEY_SEPARATOR = "|";
    private static final String KEY = "key";
    private static final String INGREDIENTS_CACHE_FILE = "cache_recipesByIngredients.json";
    private static final String INFO_CACHE_FILE = "cache_recipeInfo.json";
    private static final String INSTRUCTIONS_CACHE_FILE = "cache_recipeInstructions.json";

    private final RecipeFetcher delegate;

    private final Map<String, List<Recipe>> recipesByIngredientsCache;
    private final Map<String, Recipe> recipeInfoCache;
    private final Map<String, Recipe> recipeInstructionsCache;

    public CachingRecipeFetcher(RecipeFetcher delegate) {
        this.delegate = delegate;
        this.recipesByIngredientsCache = loadRecipesListCacheFromJson(INGREDIENTS_CACHE_FILE);
        this.recipeInfoCache = loadRecipeCacheFromJson(INFO_CACHE_FILE);
        this.recipeInstructionsCache = loadRecipeCacheFromJson(INSTRUCTIONS_CACHE_FILE);
    }

    @Override
    public List<Recipe> getRecipesByIngredients(
            List<String> ingredients, int number, int ranking, boolean ignorePantry)
            throws IngredientNotFoundException, IOException {

        final List<Recipe> result;

        final String key = String.join(",", ingredients)
                + KEY_SEPARATOR + number + KEY_SEPARATOR + ranking + KEY_SEPARATOR + ignorePantry;

        if (recipesByIngredientsCache.containsKey(key)) {
            result = recipesByIngredientsCache.get(key);
        }
        else {
            result = delegate.getRecipesByIngredients(ingredients, number, ranking, ignorePantry);
            recipesByIngredientsCache.put(key, result);
            saveRecipesListCacheToJson(recipesByIngredientsCache, INGREDIENTS_CACHE_FILE);
        }

        return result;
    }

    @Override
    public Recipe getRecipeInfo(
            int id,
            boolean includeNutrition,
            boolean addWinePairing,
            boolean addTasteData)
            throws RecipeNotFoundException, IOException {

        final Recipe result;

        final String key =
                id + KEY_SEPARATOR
                        + includeNutrition + KEY_SEPARATOR
                        + addWinePairing + KEY_SEPARATOR
                        + addTasteData;

        if (recipeInfoCache.containsKey(key)) {
            result = recipeInfoCache.get(key);
        }
        else {
            result = delegate.getRecipeInfo(id, includeNutrition, addWinePairing, addTasteData);
            recipeInfoCache.put(key, result);
            saveRecipeCacheToJson(recipeInfoCache, INFO_CACHE_FILE);
        }

        return result;
    }

    @Override
    public Recipe getRecipeInstructions(int id, boolean stepBreakdown)
            throws RecipeNotFoundException, IOException {

        final Recipe result;

        final String key = id + KEY_SEPARATOR + stepBreakdown;

        if (recipeInstructionsCache.containsKey(key)) {
            result = recipeInstructionsCache.get(key);
        }
        else {
            result = delegate.getRecipeInstructions(id, stepBreakdown);
            recipeInstructionsCache.put(key, result);
            saveRecipeCacheToJson(recipeInstructionsCache, INSTRUCTIONS_CACHE_FILE);
        }

        return result;
    }

    // --- JSON file cache helpers ---

    // For getRecipesByIngredients: maps key to List<Recipe>
    private static Map<String, List<Recipe>> loadRecipesListCacheFromJson(String filePath) {
        final Map<String, List<Recipe>> cache = new HashMap<>();
        try {
            final String content = new String(Files.readAllBytes(Paths.get(filePath)));
            final JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);
                final String key = obj.getString(KEY);
                final List<Recipe> recipes = parseRecipeList(obj.getJSONArray("recipes"));
                cache.put(key, recipes);
            }
        }
        catch (IOException ignored) {
           // Some handling
        }
        return cache;
    }

    // For getRecipeInfo and getRecipeInstructions: maps key to Recipe
    private static Map<String, Recipe> loadRecipeCacheFromJson(String filePath) {
        final Map<String, Recipe> cache = new HashMap<>();
        try {
            final String content = new String(Files.readAllBytes(Paths.get(filePath)));
            final JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                final JSONObject obj = array.getJSONObject(i);
                final String key = obj.getString(KEY);
                final Recipe recipe = parseRecipe(obj.getJSONObject("recipe"));
                cache.put(key, recipe);
            }
        }
        catch (IOException ignored) {
            // Some handling
        }
        return cache;
    }

    private static void saveRecipesListCacheToJson(Map<String, List<Recipe>> cache, String filePath) {
        final JSONArray array = new JSONArray();
        for (Map.Entry<String, List<Recipe>> entry : cache.entrySet()) {
            final JSONObject obj = new JSONObject();
            obj.put(KEY, entry.getKey());
            obj.put("recipes", serializeRecipeList(entry.getValue()));
            array.put(obj);
        }
        try {
            Files.write(Paths.get(filePath), array.toString(2).getBytes());
        }
        catch (IOException ignored) {
            // Some handling
        }
    }

    private static void saveRecipeCacheToJson(Map<String, Recipe> cache, String filePath) {
        final JSONArray array = new JSONArray();
        for (Map.Entry<String, Recipe> entry : cache.entrySet()) {
            final JSONObject obj = new JSONObject();
            obj.put(KEY, entry.getKey());
            obj.put("recipe", serializeRecipe(entry.getValue()));
            array.put(obj);
        }
        try {
            Files.write(Paths.get(filePath), array.toString(2).getBytes());
        }
        catch (IOException ignored) {
            // Some handling
        }
    }

    // --- Recipe/Ingredient JSON helpers ---

    // Converts JSONArray of recipes to List<Recipe>
    private static List<Recipe> parseRecipeList(JSONArray array) {
        final List<Recipe> recipes = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            recipes.add(parseRecipe(array.getJSONObject(i)));
        }
        return recipes;
    }

    // Converts JSONObject to Recipe object
    private static Recipe parseRecipe(JSONObject readObj) {

        final List<Ingredient> ingredients = new ArrayList<>();
        final JSONArray ingArr = readObj.optJSONArray("ingredients");

        if (ingArr != null) {
            for (int k = 0; k < ingArr.length(); k++) {
                final JSONObject ingObj = ingArr.getJSONObject(k);
                ingredients.add(
                         Ingredient.builder()
                                .setName(ingObj.getString("name"))
                                .setId(ingObj.optInt("id", -1))
                                .build()
                );
            }
        }

        return Recipe.builder()
                .setId(readObj.getInt("id"))
                .setTitle(readObj.optString("title", ""))
                .setImage(readObj.optString("image", null))
                .setIngredientNames(ingredients)
                .setHealthScore(readObj.optInt("healthScore", -1))
                .setCalories(readObj.optInt("calories", -1))
                .setInstructions(readObj.optString("instructions", null))
                .build();
    }

    // Converts Recipe object to JSONObject
    private static JSONObject serializeRecipe(Recipe recipe) {
        final JSONObject rObj = new JSONObject();
        rObj.put("id", recipe.getId());
        rObj.put("title", recipe.getTitle());
        rObj.put("image", recipe.getImage());
        final JSONArray ingArr = new JSONArray();
        for (Ingredient ing : recipe.getIngredients()) {
            final JSONObject ingObj = new JSONObject();
            ingObj.put("name", ing.getName());
            ingArr.put(ingObj);
        }
        rObj.put("ingredients", ingArr);
        rObj.put("healthScore", recipe.getHealthScore());
        rObj.put("calories", recipe.getCalories());
        rObj.put("instructions", recipe.getInstructions());
        return rObj;
    }

    // Converts List<Recipe> to JSONArray
    private static JSONArray serializeRecipeList(List<Recipe> list) {
        final JSONArray arr = new JSONArray();
        for (Recipe r : list) {
            arr.put(serializeRecipe(r));
        }
        return arr;
    }
}
