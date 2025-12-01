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

    private static final String BaseUrl = "http://172.20.10.13:3000/ingredient";

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

    /**
     * Sends a request to add an ingredient for the given user.
     *
     * @param userName       the user for whom the ingredient is added
     * @param ingredientName the name or ID of the ingredient to add
     * @throws RuntimeException if the request fails or the backend indicates failure
     */
    @Override
    public void addIngredient(String userName, String ingredientName) {
        final HttpURLConnection conn = openAddIngredientConnection();
        final JSONObject body = buildAddIngredientBody(userName, ingredientName);

        sendBody(conn, body);

        final JSONObject res = readResponseAsJson(conn);
        final boolean success = res.getBoolean(SUCCESS);

        if (!success) {
            throw new RuntimeException("Failed to add ingredient");
        }
    }

    /**
     * Opens and configures the HTTP connection used for adding an ingredient.
     *
     * @return an initialized HttpURLConnection
     * @throws RuntimeException if the URL is invalid or an I/O exception occurs
     */
    private HttpURLConnection openAddIngredientConnection() {
        try {
            final URL url = new URI(BaseUrl + "/add").toURL();
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
     * Builds the JSON body for the ingredient-add request.
     *
     * @param userName       the user to whom the ingredient is being added
     * @param ingredientName the name or ID of the ingredient
     * @return a JSONObject representing the request payload
     */
    private JSONObject buildAddIngredientBody(String userName, String ingredientName) {
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        body.put(INGREDIENT_ID, ingredientName);
        return body;
    }

    /**
     * Sends the given JSON body through an HTTP connection.
     *
     * @param conn the connection used for sending data
     * @param body the JSON payload to send
     * @throws RuntimeException if an I/O error occurs
     */
    private void sendBody(HttpURLConnection conn, JSONObject body) {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
            os.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the response from the HTTP connection and returns it as JSON.
     *
     * @param conn the HTTP connection to read from
     * @return a JSONObject representing the response body
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
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JSONObject(sb.toString());
    }

    /**
     * Creates either an input-stream reader or error-stream reader
     * depending on the HTTP status code.
     *
     * @param conn the HTTP connection to read from
     * @return a BufferedReader for the appropriate stream
     * @throws IOException if an input/output error occurs
     */
    private BufferedReader createReaderForConnection(HttpURLConnection conn) throws IOException {
        final int lower = 200;
        final int upper = 300;

        if (conn.getResponseCode() >= lower && conn.getResponseCode() < upper) {
            return new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        return new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }

    /**
     * Retrieves all ingredients for the given user from the backend service.
     *
     * @param userName the user whose ingredients should be loaded
     * @return a list of Ingredient objects belonging to the user
     * @throws RuntimeException if the HTTP request fails or the response indicates failure
     */
    @Override
    public List<Ingredient> getAllIngredients(String userName) {
        final HttpURLConnection conn = openGetIngredientsConnection(userName);
        final JSONObject body = buildUserNameBody(userName);

        sendBody(conn, body);

        final JSONObject res = readResponseAsJson(conn);
        final boolean success = res.getBoolean(SUCCESS);

        // 保持原来的行为：失败时抛出同样的异常信息
        if (!success) {
            throw new RuntimeException("Failed to add recipe");
        }

        final JSONArray ingredientsArray = res.getJSONArray("ingredients");
        return parseIngredientsArray(ingredientsArray);
    }

    /**
     * Opens and configures the HTTP connection used to fetch all ingredients
     * for a given user.
     *
     * @param userName the user whose ingredients are being requested
     * @return an initialized HttpURLConnection
     * @throws RuntimeException if the URL is invalid or an I/O error occurs
     */
    private HttpURLConnection openGetIngredientsConnection(String userName) {
        try {
            final URL url = new URI(BaseUrl + "/" + userName).toURL();
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
     * Builds a simple JSON body containing only the user name.
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
     * Parses the ingredients array from the backend response into a list of
     * {@link Ingredient} objects.
     *
     * @param ingredientsArray the JSON array containing ingredient objects
     * @return a list of Ingredient instances
     */
    private List<Ingredient> parseIngredientsArray(JSONArray ingredientsArray) {
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

    /**
     * Sends a request to delete an ingredient for the given user.
     *
     * @param userName     the user from whom the ingredient will be removed
     * @param ingredientID the ID of the ingredient to delete
     * @throws RuntimeException if the request fails or the backend reports failure
     */
    @Override
    public void deleteIngredient(String userName, int ingredientID) {
        final HttpURLConnection conn = openDeleteIngredientConnection();
        final JSONObject body = buildDeleteIngredientBody(userName, ingredientID);

        sendBody(conn, body);

        final JSONObject res = readResponseAsJson(conn);
        final boolean success = res.getBoolean(SUCCESS);

        if (!success) {
            throw new RuntimeException("Failed to delete ingredient");
        }
    }

    /**
     * Opens and configures the HTTP connection used to delete an ingredient.
     *
     * @return an initialized HttpURLConnection
     * @throws RuntimeException if the URL is invalid or an I/O error occurs
     */
    private HttpURLConnection openDeleteIngredientConnection() {
        try {
            final URL url = new URI(BaseUrl + "/delete").toURL();
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
     * Builds the JSON body for the delete-ingredient request.
     *
     * @param userName     the user from whom the ingredient is removed
     * @param ingredientID the ID of the ingredient to delete
     * @return a JSONObject representing the request payload
     */
    private JSONObject buildDeleteIngredientBody(String userName, int ingredientID) {
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        body.put(INGREDIENT_ID, ingredientID);
        return body;
    }

    @Override
    public boolean exists(String userName, int ingredientID) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BaseUrl + userName + "/exists/" + ingredientID).toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(false);
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println(INVALID_URI_SYNTAX);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
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
        catch (IOException event) {
            throw new RuntimeException(event);
        }

        final JSONObject res = new JSONObject(sb.toString());

        return res.getBoolean("exists");
    }
}
