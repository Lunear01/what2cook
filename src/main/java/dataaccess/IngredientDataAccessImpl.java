package dataaccess;

import entity.Ingredient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IngredientDataAccessImpl implements IngredientDataAccess {

    private final String baseUrl = "http://localhost:3000/ingredient";

    @Override
    public void addIngredient(String user_name, int ingredientID, String ingredient_name) throws Exception {

        final URL url = new URL(baseUrl + "/add");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", user_name);
        body.put("ingredient_name", ingredient_name);
        body.put("ingredient_id", ingredientID);

        final OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes());
        os.flush();

        conn.getInputStream();
    }

    @Override
    public List<Ingredient> getAllIngredients(String user_name) throws Exception {

        final URL url = new URL(baseUrl + "/" + user_name);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", user_name);

        final OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes());
        os.flush();

        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        final JSONObject res = new JSONObject(sb.toString());

        final JSONArray ingredientsArray = res.getJSONArray("ingredients");

        final List<Ingredient> ingredientList = new ArrayList<>();

        for (int i = 0; i < ingredientsArray.length(); i++) {
            final JSONObject obj = ingredientsArray.getJSONObject(i);
            final Ingredient ing = Ingredient.builder()
                    .setName(obj.getString("ingredient_name"))
                    .setIngredientId(obj.getInt("ingredient_id"))
                    .build();
            ingredientList.add(ing);
        }

        return ingredientList;
    }

    @Override
    public void deleteIngredient(String user_name, int ingredientID) throws Exception {
        final URL url = new URL(baseUrl + "/delete");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", user_name);
        body.put("ingredient_id", ingredientID);

        final OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes());
        os.flush();

        conn.getInputStream();
    }
}
