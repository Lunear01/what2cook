package dataaccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

import entity.User;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class UserDataAccesssObject implements
        LoginUserDataAccessInterface, SignupUserDataAccessInterface {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";

    private static final String BASE_URL = "http://172.20.10.3:3000/user";

    @Override
    public void save(User user) {

        try {
            final URL url = new URI(BASE_URL + "/signup").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(POST);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            final JSONObject body = new JSONObject();
            body.put("user_name", user.getName());
            body.put("email", user.getEmail());
            body.put("password", user.getPassword());

            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            conn.getInputStream();
        }
        catch (URISyntaxException uriSyntaxException) {
            System.out.println("Invalid URI syntax");
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    public User get(String userName, String password) {
        HttpURLConnection conn;
        try {
            final URL url = new URI(BASE_URL + "/login").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
        }
        catch (URISyntaxException uriSyntaxException) {
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            throw new RuntimeException(ioException);
        }

        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("password", password);
        StringBuilder sb;
        try {
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
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        final JSONObject res = new JSONObject(sb.toString());

        final JSONObject userJson = res.getJSONObject("user");

        return User.builder()
                .withName(userJson.getString("user_name"))
                .withEmail(userJson.getString("email"))
                .withPassword(userJson.getString("password"))
                .build();
    }

    @Override
    public boolean existsByName(String userName) {
        URL url;
        try {
            url = new URI(BASE_URL + "/exists").toURL();
        }
        catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            throw new RuntimeException(e);
        }

        HttpURLConnection conn;
        StringBuilder sb;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
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
        catch (IOException e) {
            System.out.println("Error opening connection: " + e.getMessage());
            throw new RuntimeException(e);
        }

        final JSONObject res = new JSONObject(sb.toString());

        return res.getBoolean("exists");
    }
}
