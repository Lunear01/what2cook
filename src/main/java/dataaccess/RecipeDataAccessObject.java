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

    private final String baseUrl = "http://192.168.2.13:3000/recipe";

    @Override
    public void addRecipe(String userName, Recipe recipe) {
        try {
            final URL url = new URI(baseUrl + "/add").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000); // 5 seconds
            conn.setReadTimeout(10000);   // 10 seconds

            final JSONArray ingredients = new JSONArray();
            for  (Ingredient ingredient : recipe.getIngredients()) {
                final JSONObject ing = new JSONObject();
                ing.put("name", ingredient.getName());
                ingredients.put(ing);  // Bug #1 修复: 添加 ingredient 到 array
            }

            final JSONObject recipeJson = new JSONObject();
            recipeJson.put("image", recipe.getImage());
            recipeJson.put("ingredients", ingredients);
            recipeJson.put("id", recipe.getId());
            recipeJson.put("calories", recipe.getCalories());
            recipeJson.put("instructions", recipe.getInstructions());
            recipeJson.put("healthScore", recipe.getHealthScore());

            final JSONObject body = new JSONObject();
            body.put("user_name", userName);
            body.put("recipe_id", recipe.getId());
            body.put("recipe", recipeJson);

            // Bug #5 修复: 使用 try-with-resources 自动关闭 OutputStream
            try (final OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes());
                os.flush();
            }

            // Bug #3 修复: 检查 HTTP 状态码
            final int responseCode = conn.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                throw new RuntimeException("HTTP error code: " + responseCode);
            }
            conn.getInputStream();
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Recipe> getAllRecipes(String userName) {
        StringBuilder sb;
        try {
            final URL url = new URI(baseUrl + "/" + userName).toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5000); // 5 seconds
            conn.setReadTimeout(10000);   // 10 seconds

            // Bug #19 修复: GET 请求不应该有 body，已删除

            // Bug #3 修复: 检查 HTTP 状态码，404 对首次用户是正常的
            final int responseCode = conn.getResponseCode();
            if (responseCode == 404) {
                // 404 is expected for first-time users
                return new ArrayList<>();
            }
            if (responseCode < 200 || responseCode >= 300) {
                throw new RuntimeException("HTTP error code: " + responseCode);
            }

            // Bug #6 修复: 使用 try-with-resources 自动关闭 BufferedReader
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        final JSONObject res = new JSONObject(sb.toString());

        final JSONArray recipesArray = res.getJSONArray("recipes");

        final List<Recipe> recipeList = new ArrayList<>();

        for (int i = 0; i < recipesArray.length(); i++) {

            final JSONObject obj = recipesArray.getJSONObject(i);
            final List<Ingredient> ingredientList = new ArrayList<>();
            final JSONArray ingredientsArray = obj.getJSONArray("ingredientNames");

            for (int j = 0; j < ingredientsArray.length(); j++) {
                final JSONObject object = ingredientsArray.getJSONObject(j);  // Bug #2 修复: 使用 j 而不是 i
                final Ingredient ing = Ingredient.builder()
                        .setName(object.getString("ingredient_name"))
                        .setId(object.getInt("ingredient_id"))
                        .build();
                ingredientList.add(ing);
            }
            final Recipe ing = Recipe.builder()
                    .setId(obj.getInt("recipeID"))
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
        try {
            final URL url = new URI(baseUrl + "/delete").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000); // 5 seconds
            conn.setReadTimeout(10000);   // 10 seconds

            final JSONObject body = new JSONObject();
            body.put("user_name", user_name);
            body.put("recipe_id", recipeID);

            // Bug #5 修复: 使用 try-with-resources 自动关闭 OutputStream
            try (final OutputStream os = conn.getOutputStream()) {
                os.write(body.toString().getBytes());
                os.flush();
            }

            // Bug #3 修复: 检查 HTTP 状态码
            final int responseCode = conn.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                throw new RuntimeException("HTTP error code: " + responseCode);
            }
            conn.getInputStream();
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
