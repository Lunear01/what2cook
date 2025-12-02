package dataaccess;

import entity.Ingredient;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.cookinglist.RecipeDataAccessInterface;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeDataAccessObject implements RecipeDataAccessInterface {

    private final String BASE_URL = Constance.baseUrl + "recipe";

    @Override
    public void addRecipe(String userName, Recipe recipe) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BASE_URL + "/add").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Constance.POST);
            conn.setRequestProperty(Constance.Content_Type, Constance.Content_Type_JSON);
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(Constance.invalid_URI_syntax);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        final JSONArray ingredients = new JSONArray();
        for (Ingredient ingredient : recipe.getIngredients()) {
            final JSONObject ing = new JSONObject();
            ing.put("ingredient_name", ingredient.getName());
            ingredients.put(ing);
        }

        final JSONObject recipeJson = new JSONObject();
        recipeJson.put("title", recipe.getTitle());
        recipeJson.put("image", recipe.getImage());
        recipeJson.put("ingredients", ingredients);
        recipeJson.put("recipe_id", recipe.getId());
        recipeJson.put("calories", recipe.getCalories());
        recipeJson.put("instructions", recipe.getInstructions());
        recipeJson.put("healthScore", recipe.getHealthScore());

        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("recipe_id", recipe.getId());
        body.put("recipes", recipeJson);

        try {
            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        final StringBuilder sb = new StringBuilder();
        try {
            final BufferedReader br;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        final JSONObject res = new JSONObject(sb.toString());

        final boolean success = res.getBoolean("success");

        if (!success) {
            throw new RuntimeException("Failed to add recipe");
        }
    }

    @Override
    public List<Recipe> getAllRecipes(String userName) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BASE_URL + "/" + userName).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Constance.GET);
            conn.setRequestProperty(Constance.Content_Type, Constance.Content_Type_JSON);
            conn.setDoOutput(false);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(Constance.invalid_URI_syntax);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        final StringBuilder sb = new StringBuilder();
        try {
            final BufferedReader br;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        final JSONObject res = new JSONObject(sb.toString());

        final boolean success = res.getBoolean("success");

        if (!success) {
            throw new RuntimeException("Failed to get recipe");
        }

        final JSONArray recipesArray = res.getJSONArray("recipes");

        final List<Recipe> recipeList = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {

            final JSONObject obj = recipesArray.getJSONObject(i);
            final List<Ingredient> ingredientList = new ArrayList<>();
            final JSONObject recipeObj = obj.getJSONObject("recipes");
            final JSONArray ingredientsArray = recipeObj.getJSONArray("ingredients");

            for (int j = 0; j < ingredientsArray.length(); j++) {
                final JSONObject object = ingredientsArray.getJSONObject(j);
                final Ingredient ing = Ingredient.builder()
                        .setName(object.getString("ingredient_name"))
                        .build();
                ingredientList.add(ing);
            }
            final Recipe ing = Recipe.builder()
                    .setId(obj.getInt("recipe_id"))
                    .setTitle(obj.getString("title"))
                    // may have some issue will fix later
                    .setIngredientNames(ingredientList)
                    .setCalories(obj.getDouble("calories"))
                    .setHealthScore(obj.getInt("healthScore"))
                    .setInstructions(obj.getString("instructions"))
                    .setImage(obj.getString("image"))
                    .build();
            recipeList.add(ing);
        }
        return recipeList;
    }

    @Override
    public void deleteRecipe(String user_name, int recipeID) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BASE_URL + "/delete").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Constance.DELETE);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(Constance.invalid_URI_syntax);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        final JSONObject body = new JSONObject();
        body.put("user_name", user_name);
        body.put("recipe_id", recipeID);

        try {
            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        final StringBuilder sb = new StringBuilder();
        try {
            final BufferedReader br;

            if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException event) {
            throw new RuntimeException(event);
        }

        final JSONObject res = new JSONObject(sb.toString());

        final boolean success = res.getBoolean("success");

        if (!success) {
            throw new RuntimeException("Failed to delete recipe");
        }
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