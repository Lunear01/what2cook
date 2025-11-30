package dataaccess;

import entity.Ingredient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IngredientDataAccessObject implements IngredientDataAccessInterface {

    private static final String BASE_URL = "http://172.20.10.7:3000/ingredient";

    @Override
    public void addIngredient(String userName, int ingredientID, String ingredientName) {
        try {
            final URL url = new URL(BASE_URL + "/add");
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            final JSONObject body = new JSONObject();
            body.put("user_name", userName);
            body.put("ingredient_name", ingredientName);
            body.put("ingredient_id", ingredientID);

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            conn.getInputStream();
        }
        catch (IOException ioException) {
            System.out.println("Error opening connection: " + ioException.getMessage());
            ioException.printStackTrace();
            throw new RuntimeException(ioException);
        }
    }

    @Override
    public List<Ingredient> getAllIngredients(String userName) {
        try {
            final URL url = new URL(BASE_URL + "/" + userName);
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
                        .setId(obj.getInt("ingredient_id"))
                        .build();
                ingredientList.add(ing);
            }
            return ingredientList;
        }
        catch (IOException ioException) {
            System.out.println("Error opening connection: " + ioException.getMessage());
            ioException.printStackTrace();
            throw new RuntimeException(ioException);
        }
    }

    @Override
    public void deleteIngredient(String userName, int ingredientID) {
        try {
            final URL url = new URL(BASE_URL + "/delete");
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            final JSONObject body = new JSONObject();
            body.put("user_name", userName);
            body.put("ingredient_id", ingredientID);

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            conn.getInputStream();
        }
        catch (IOException ioException) {
            System.out.println("Error opening connection: " + ioException.getMessage());
            ioException.printStackTrace();
            throw new RuntimeException(ioException);
        }

    }
}
