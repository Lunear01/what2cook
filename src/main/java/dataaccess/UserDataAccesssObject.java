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

    private static final String BASE_URL = Constance.baseUrl + "user";

    @Override
    public void save(User user) {

        try {
            final URL url = new URI(BASE_URL + "/signup").toURL();
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Constance.POST);
            conn.setDoOutput(true);
            conn.setRequestProperty(Constance.Content_Type, Constance.Content_Type_JSON);

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
            System.out.println(Constance.invalid_URI_syntax);
            throw new RuntimeException(uriSyntaxException);
        }
        catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @Override
    public User get(String userName, String password) {
        final HttpURLConnection conn;
        try {
            final URL url = new URI(BASE_URL + "/login").toURL();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Constance.POST);
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
        body.put("password", password);

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

        final String message = res.getString("message");

        if (!message.equals(Constance.login_success)) {
            return null; // may have issue look later.
        }

        final JSONObject userJson = res.getJSONObject("user");

        return User.builder()
                .withName(userJson.getString("user_name"))
                .withEmail(userJson.getString("email"))
                .withPassword(userJson.getString("password"))
                .build();
    }

    @Override
    public boolean existsByName(String userName) {
        final URL url;
        try {
            url = new URI(BASE_URL + "/" + userName).toURL();
        }
        catch (URISyntaxException event) {
            System.out.println(event.getMessage());
            throw new RuntimeException(event);
        }
        catch (MalformedURLException event) {
            System.out.println("Malformed URL");
            throw new RuntimeException(event);
        }

        final HttpURLConnection conn;
        final StringBuilder sb;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(Constance.GET);
            conn.setRequestProperty(Constance.Content_Type, Constance.Content_Type_JSON);
            conn.setDoOutput(false);

            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (IOException event) {
            System.out.println("Error opening connection: " + event.getMessage());
            throw new RuntimeException(event);
        }

        final JSONObject res = new JSONObject(sb.toString());

        return res.getBoolean("exists");
    }
}