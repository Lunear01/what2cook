package dataaccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Ingredient;
import entity.Recipe;
import use_case.cookinglist.RecipeDataAccessInterface;

public class RecipeDataAccessObject implements RecipeDataAccessInterface {

    private static final String SUCCESS = "success";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String INVALID_URI_SYNTAX = "Invalid URI syntax";
    private static final String RECIPE_ID = "recipe_id";
    private static final String USER_NAME = "user_name";

    private final String BASE_URL = "http://172.20.10.13:3000/recipe";

    @Override
    public void addRecipe(String userName, Recipe recipe) {
        final HttpURLConnection conn = openAddConnection();

        final JSONArray ingredients = buildIngredientsArray(recipe);
        final JSONObject recipeJson = buildRecipeJson(recipe, ingredients);
        final JSONObject body = buildRequestBody(userName, recipe, recipeJson);

        sendBody(conn, body);

        final JSONObject res = readResponseAsJson(conn);
        final boolean success = res.getBoolean(SUCCESS);

        if (!success) {
            throw new RuntimeException("Failed to add recipe");
        }
    }

    /**
     * Opens and configures the HTTP connection for adding a recipe.
     *
     * @throws RuntimeException throw the exception.
     */
    private HttpURLConnection openAddConnection() {
        try {
            final URL url = new URI(BASE_URL + "/add").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(POST);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);
            return conn;
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /**
     * Builds the JSON array of ingredients for the given recipe.
     *
     * @param recipe the recipe whose ingredients should be converted
     */
    private JSONArray buildIngredientsArray(Recipe recipe) {
        final JSONArray ingredients = new JSONArray();
        for (Ingredient ingredient : recipe.getIngredients()) {
            final JSONObject ing = new JSONObject();
            ing.put("ingredient_name", ingredient.getName());
            ing.put("ingredient_id", ingredient.getIngredientId());
            ingredients.put(ing);
        }
        return ingredients;
    }

    /**
     * Builds the JSON object representing the recipe itself.
     *
     * @param recipe      the recipe being added
     * @param ingredients the JSON array of ingredient objects
     */
    private JSONObject buildRecipeJson(Recipe recipe, JSONArray ingredients) {
        final JSONObject recipeJson = new JSONObject();
        recipeJson.put("title", recipe.getTitle());
        recipeJson.put("image", recipe.getImage());
        recipeJson.put("ingredients", ingredients);
        recipeJson.put(RECIPE_ID, recipe.getId());
        recipeJson.put("calories", recipe.getCalories());
        recipeJson.put("instructions", recipe.getInstructions());
        recipeJson.put("healthScore", recipe.getHealthScore());
        return recipeJson;
    }

    /**
     * Builds the full request body JSON with user and recipe information.
     *
     * @param userName   the user to whom the recipe belongs
     * @param recipe     the recipe being added
     * @param recipeJson the full recipe JSON object
     */
    private JSONObject buildRequestBody(String userName, Recipe recipe, JSONObject recipeJson) {
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        body.put(RECIPE_ID, recipe.getId());
        body.put("recipes", recipeJson);
        return body;
    }

    /**
     * Writes the JSON body to the HTTP connection.
     *
     * @param conn the HTTP connection to write to
     * @param body the JSON body to send
     * @throws RuntimeException if an I/O error occurs
     */
    private void sendBody(HttpURLConnection conn, JSONObject body) {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
            os.flush();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Reads the HTTP response and returns it as a JSON object.
     *
     * @param conn the HTTP connection from which to read the response
     * @throws RuntimeException if an I/O error occurs
     */
    private JSONObject readResponseAsJson(HttpURLConnection conn) {
        final StringBuilder sb = new StringBuilder();
        try (BufferedReader br = createReaderForConnection(conn)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return new JSONObject(sb.toString());
    }

    /**
     * Creates the appropriate reader depending on the HTTP status code.
     *
     * @param conn the HTTP connection
     * @return a BufferedReader for reading the response
     * @throws IOException if stream access fails
     */
    private BufferedReader createReaderForConnection(HttpURLConnection conn) throws IOException {
        final int lowerLimit = 200;
        final int upperLimit = 300;

        if (conn.getResponseCode() >= lowerLimit && conn.getResponseCode() < upperLimit) {
            return new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        return new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }

    /**
     * Retrieves all recipes for the given user from the backend service.
     *
     * @param userName the user whose recipes should be loaded
     * @return a list of Recipe objects belonging to the user
     * @throws RuntimeException if the HTTP request fails or the response indicates failure
     */
    @Override
    public List<Recipe> getAllRecipes(String userName) {
        final JSONObject body = buildUserNameBody(userName);
        final JSONObject response = sendGetAllRecipesRequest(userName, body);
        ensureGetRecipesSuccess(response);

        final JSONArray recipesArray = response.getJSONArray("recipes");
        return parseRecipesArray(recipesArray);
    }

    /**
     * Sends the request to get all recipes for the given user.
     *
     * @param userName the user whose recipes are requested
     * @param body     the JSON request body containing the username
     * @return the JSON response from the backend
     * @throws RuntimeException if an I/O error occurs
     */
    private JSONObject sendGetAllRecipesRequest(String userName, JSONObject body) {
        final HttpURLConnection conn = openGetRecipesConnection(userName);
        sendBody(conn, body);
        return readResponseAsJson(conn);
    }

    /**
     * Opens and configures the HTTP connection used to fetch all recipes.
     *
     * @param userName the user whose recipes are being requested
     * @return an initialized HttpURLConnection
     * @throws RuntimeException if the URL is invalid or an I/O error occurs
     */
    private HttpURLConnection openGetRecipesConnection(String userName) {
        try {
            final URL url = new URI(BASE_URL + "/" + userName).toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);
            return conn;
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /**
     * Builds a simple JSON body containing only the username.
     *
     * @param userName the user whose data is being requested
     * @return a JSONObject representing the request body
     */
    private JSONObject buildUserNameBody(String userName) {
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        return body;
    }

    /**
     * Verifies that the get-recipes operation succeeded.
     *
     * @param response the JSON response from the backend
     * @throws RuntimeException if the response indicates failure
     */
    private void ensureGetRecipesSuccess(JSONObject response) {
        final boolean success = response.getBoolean(SUCCESS);
        if (!success) {
            throw new RuntimeException("Failed to get recipe");
        }
    }

    /**
     * Parses the JSON array of recipes into a list of {@link Recipe} objects.
     *
     * @param recipesArray the JSON array containing recipe objects
     * @return a list of Recipe instances
     */
    private List<Recipe> parseRecipesArray(JSONArray recipesArray) {
        final List<Recipe> recipeList = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {
            final JSONObject obj = recipesArray.getJSONObject(i);
            final JSONArray ingredientsArray = obj.getJSONArray("ingredients");
            final List<Ingredient> ingredients = parseRecipeIngredients(ingredientsArray);
            final Recipe recipe = buildRecipeFromJson(obj, ingredients);
            recipeList.add(recipe);
        }
        return recipeList;
    }

    /**
     * Parses the ingredients of a single recipe from a JSON array.
     *
     * @param ingredientsArray the JSON array containing ingredient objects
     * @return a list of Ingredient instances
     */
    private List<Ingredient> parseRecipeIngredients(JSONArray ingredientsArray) {
        final List<Ingredient> ingredientList = new ArrayList<>();

        for (int j = 0; j < ingredientsArray.length(); j++) {
            final JSONObject object = ingredientsArray.getJSONObject(j);
            final Ingredient ing = Ingredient.builder()
                    .setName(object.getString("ingredient_name"))
                    .setId(object.getInt("ingredient_id"))
                    .build();
            ingredientList.add(ing);
        }
        return ingredientList;
    }

    /**
     * Builds a {@link Recipe} object from a JSON representation and its ingredients.
     *
     * @param obj         the JSON object containing the recipe fields
     * @param ingredients the list of ingredients belonging to the recipe
     * @return a constructed Recipe instance
     */
    private Recipe buildRecipeFromJson(JSONObject obj, List<Ingredient> ingredients) {
        return Recipe.builder()
                .setId(obj.getInt(RECIPE_ID))
                .setTitle(obj.getString("title"))
                .setIngredientNames(ingredients)
                .setCalories(obj.getDouble("calories"))
                .setHealthScore(obj.getInt("healthScore"))
                .setInstructions(obj.getString("instructions"))
                .setImage(obj.getString("image"))
                .build();
    }

    /**
     * Sends a request to delete a recipe for the given user.
     *
     * @param user_name the user from whom the recipe will be deleted
     * @param recipeID  the ID of the recipe to delete
     * @throws RuntimeException if the request fails or the backend reports failure
     */
    @Override
    public void deleteRecipe(String user_name, int recipeID) {
        final JSONObject body = buildDeleteRecipeBody(user_name, recipeID);
        final JSONObject response = sendDeleteRecipeRequest(body);
        ensureDeleteRecipeSuccess(response);
    }

    /**
     * Sends the delete-recipe request and returns the backend response.
     *
     * @param body the JSON body containing user and recipe information
     * @return the JSON response from the backend
     * @throws RuntimeException if an I/O error occurs
     */
    private JSONObject sendDeleteRecipeRequest(JSONObject body) {
        final HttpURLConnection conn = openDeleteRecipeConnection();
        sendBody(conn, body);
        return readResponseAsJson(conn);
    }

    /**
     * Verifies that the delete-recipe operation was successful.
     *
     * @param response the JSON response from the backend
     * @throws RuntimeException if the response indicates failure
     */
    private void ensureDeleteRecipeSuccess(JSONObject response) {
        final boolean success = response.getBoolean(SUCCESS);
        if (!success) {
            throw new RuntimeException("Failed to delete recipe");
        }
    }

    /**
     * Opens and configures the HTTP connection used to delete a recipe.
     *
     * @return an initialized HttpURLConnection
     * @throws RuntimeException if the URL is invalid or an I/O error occurs
     */
    private HttpURLConnection openDeleteRecipeConnection() {
        try {
            final URL url = new URI(BASE_URL + "/delete").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(DELETE);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);
            return conn;
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /**
     * Builds the JSON body for the delete-recipe request.
     *
     * @param user_name the user from whom the recipe is removed
     * @param recipeID  the ID of the recipe to delete
     * @return a JSONObject representing the request payload
     */
    private JSONObject buildDeleteRecipeBody(String user_name, int recipeID) {
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, user_name);
        body.put(RECIPE_ID, recipeID);
        return body;
    }

//    @Override
//    public boolean exists(String userName, int recipeID) {
//        final HttpURLConnection conn;
//        try {
//            final URL url = new URI(BASE_URL + userName + "/exists/" + recipeID).toURL();
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod(GET);
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setDoOutput(false);
//        }
//        catch (URISyntaxException uriSyntaxException) {
//            System.out.println("Invalid URI syntax");
//            throw new RuntimeException(uriSyntaxException);
//        }
//        catch (IOException ioException) {
//            throw new RuntimeException(ioException);
//        }
//
//        final StringBuilder sb = new StringBuilder();
//
//        try {
//            final BufferedReader br;
//
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            }
//            else {
//                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//        }
//        catch (IOException event) {
//            throw new RuntimeException(event);
//        }
//
//        final JSONObject res = new JSONObject(sb.toString());
//
//        return res.getBoolean("exists");
//    }
}
