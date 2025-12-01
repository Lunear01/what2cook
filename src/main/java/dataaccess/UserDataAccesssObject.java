package dataaccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.json.JSONObject;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class UserDataAccesssObject implements
        LoginUserDataAccessInterface, SignupUserDataAccessInterface {

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String INVALID_URI_SYNTAX = "Invalid URI syntax";
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";

    private static final String baseUrl = "http://172.20.10.13:3000/user";

    @Override
    public void save(User user) {

        try {
            final URL url = new URI(baseUrl + "/signup").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(POST);
            conn.setDoOutput(true);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);

            final JSONObject body = new JSONObject();
            body.put(USER_NAME, user.getName());
            body.put("email", user.getEmail());
            body.put(PASSWORD, user.getPassword());

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            conn.getInputStream();
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
     * Logs in the user with the given credentials and returns the User object
     * if the login is successful; otherwise returns {@code null}.
     *
     * @param userName the user name used to log in
     * @param password the password used to log in
     * @return the logged-in User, or {@code null} if login fails
     * @throws RuntimeException if the HTTP request fails
     */
    @Override
    public User get(String userName, String password) {
        final JSONObject response = sendLoginRequest(userName, password);
        return parseLoginResponse(response);
    }

    /**
     * Sends the login request to the backend and returns the JSON response.
     *
     * @param userName the user name used to log in
     * @param password the password used to log in
     * @return the JSON response from the backend
     * @throws RuntimeException if an I/O error occurs
     */
    private JSONObject sendLoginRequest(String userName, String password) {
        final HttpURLConnection conn = openLoginConnection();
        final JSONObject body = buildLoginBody(userName, password);

        sendBody(conn, body);
        return readResponseAsJson(conn);
    }

    /**
     * Opens and configures the HTTP connection used for the login request.
     *
     * @return an initialized HttpURLConnection
     * @throws RuntimeException if the URL is invalid or an I/O error occurs
     */
    private HttpURLConnection openLoginConnection() {
        try {
            final URL url = new URI(baseUrl + "/login").toURL();
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
     * Builds the JSON body for the login request.
     *
     * @param userName the username used to log in
     * @param password the password used to log in
     * @return a JSONObject representing the request body
     */
    private JSONObject buildLoginBody(String userName, String password) {
        final JSONObject body = new JSONObject();
        body.put(USER_NAME, userName);
        body.put(PASSWORD, password);
        return body;
    }

    /**
     * Parses the login response and returns the corresponding User object,
     * or {@code null} if the login was not successful.
     *
     * @param response the JSON response from the backend
     * @return the logged-in User, or {@code null} if login failed
     */
    private User parseLoginResponse(JSONObject response) {
        final String message = response.getString("message");
        User user = null;

        if ("Login success".equals(message)) {
            final JSONObject userJson = response.getJSONObject("user");
            user = User.builder()
                    .withName(userJson.getString(USER_NAME))
                    .withEmail(userJson.getString("email"))
                    .withPassword(userJson.getString(PASSWORD))
                    .build();
        }

        return user;
    }

    /**
     * Sends a JSON body over the given HTTP connection.
     *
     * @param conn the HTTP connection
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
     * @param conn the HTTP connection
     * @return a JSONObject representing the response
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
     * Creates the appropriate reader (input or error stream) depending on status code.
     *
     * @param conn the HTTP connection
     * @return a BufferedReader for the correct stream
     * @throws IOException if an I/O error occurs
     */
    private BufferedReader createReaderForConnection(HttpURLConnection conn) throws IOException {
        final int lower = 200;
        final int upper = 300;

        if (conn.getResponseCode() >= lower && conn.getResponseCode() < upper) {
            return new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        return new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }

    @Override
    public boolean existsByName(String userName) {
        final URL url;
        try {
            url = new URI(baseUrl + "/" + userName).toURL();
        }
        catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            throw new RuntimeException(e);
        }

        final HttpURLConnection conn;
        final StringBuilder sb;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(false);

            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException e) {
            System.out.println("Error opening connection: " + e.getMessage());
            throw new RuntimeException(e);
        }

        final JSONObject res = new JSONObject(sb.toString());

        return res.getBoolean("exists");
    }
}
