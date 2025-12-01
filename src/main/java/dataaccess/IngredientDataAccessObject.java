package dataaccess;

import entity.Ingredient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IngredientDataAccessObject implements IngredientDataAccessInterface {

    private static final String BaseUrl = "http://172.20.10.3:3000/ingredient";

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    @Override
    public int addIngredient(String userName, String ingredientName) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BaseUrl + "/add").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(POST);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println("Invalid URI syntax");
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("ingredient_name", ingredientName);

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
            throw new RuntimeException("Failed to add ingredient");
        }
        return res.getInt("ingredient_id");
    }

    @Override
    public List<Ingredient> getAllIngredients(String userName) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BaseUrl + "/" + userName).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(false);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println("Invalid URI syntax");
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
        catch (IOException event) {
            throw new RuntimeException(event);
        }

        final JSONObject res = new JSONObject(sb.toString());

        final boolean success = res.getBoolean("success");

        if (!success) {
            throw new RuntimeException("Failed to add recipe");
        }

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

    @Override
    public void deleteIngredient(String userName, int ingredientID) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BaseUrl + "/delete").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(DELETE);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println("Invalid URI syntax");
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("ingredient_id", ingredientID);

        try {
            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();
        }
        catch (IOException event) {
            throw new RuntimeException(event);
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
            throw new RuntimeException("Failed to delete ingredient");
        }
    }

    @Override
    public boolean exists(String userName, int ingredientID) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BaseUrl + userName + "/exists/" + ingredientID).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(false);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println("Invalid URI syntax");
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
        catch (IOException event) {
            throw new RuntimeException(event);
        }

        final JSONObject res = new JSONObject(sb.toString());

        return res.getBoolean("exists");
    }
}