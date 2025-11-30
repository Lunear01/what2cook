package dataaccess;

import entity.Ingredient;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeDataAccessObject implements RecipeDataAccessInterface {

    private final String baseUrl = "http://172.20.10.3:3000/recipe";

    @Override
    public void addRecipe(String userName, int recipeID, JSONObject recipe) {
        try {
            final URL url = new URI(baseUrl + "/add").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            final JSONObject body = new JSONObject();
            body.put("user_name", userName);
            body.put("recipe", recipe);
            body.put("recipe_id", recipeID);

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

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
            conn.setDoOutput(true);

            final JSONObject body = new JSONObject();
            body.put("user_name", userName);

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
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
                final JSONObject object = ingredientsArray.getJSONObject(i);
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

            final JSONObject body = new JSONObject();
            body.put("user_name", user_name);
            body.put("recipe_id", recipeID);

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            conn.getInputStream();
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
