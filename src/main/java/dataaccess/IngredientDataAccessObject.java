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

public class IngredientDataAccessObject implements IngredientDataAccessInterface {

    private static final String baseUrl = "http://172.20.10.7:3000/ingredient";

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String INVALID_URI_SYNTAX = "Invalid URI syntax";
    private static final String USER_NAME = "user_name";
    private static final String INGREDIENT_ID = "ingredient_id";
    private static final String INGREDIENT_NAME = "ingredient_name";
    private static final String SUCCESS = "success";

    @Override
    public void addIngredient(String userName, int ingredientID, String ingredientName) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(baseUrl + "/add").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(POST);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        body.put(INGREDIENT_ID, ingredientName);
        body.put(INGREDIENT_NAME, ingredientID);

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
            final int lowerLimit = 200;
            final int upperLimit = 300;
            if (conn.getResponseCode() >= lowerLimit && conn.getResponseCode() < upperLimit) {
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

        final boolean success = res.getBoolean(SUCCESS);

        if (!success) {
            throw new RuntimeException("Failed to add ingredient");
        }
    }

    @Override
    public List<Ingredient> getAllIngredients(String userName) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(baseUrl + "/add").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);

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
            final int lowerLimit = 200;
            final int upperLimit = 300;
            if (conn.getResponseCode() >= lowerLimit && conn.getResponseCode() < upperLimit) {
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

        final boolean success = res.getBoolean(SUCCESS);

        if (!success) {
            throw new RuntimeException("Failed to add recipe");
        }

        final JSONArray ingredientsArray = res.getJSONArray("ingredients");

        final List<Ingredient> ingredientList = new ArrayList<>();

        for (int i = 0; i < ingredientsArray.length(); i++) {
            final JSONObject obj = ingredientsArray.getJSONObject(i);
            final Ingredient ing = Ingredient.builder()
                    .setName(obj.getString(INGREDIENT_NAME))
                    .setId(obj.getInt(INGREDIENT_ID))
                    .build();
            ingredientList.add(ing);
        }
        return ingredientList;
    }

    @Override
    public void deleteIngredient(String userName, int ingredientID) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(baseUrl + "/delete").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(DELETE);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        body.put(INGREDIENT_ID, ingredientID);

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
            final int lowerLimit = 200;
            final int upperLimit = 300;
            if (conn.getResponseCode() >= lowerLimit && conn.getResponseCode() < upperLimit) {
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

        final boolean success = res.getBoolean(SUCCESS);

        if (!success) {
            throw new RuntimeException("Failed to delete ingredient");
        }
    }
}