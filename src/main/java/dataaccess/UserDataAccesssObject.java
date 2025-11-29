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

    private final String baseUrl = "http://localhost:3000/user";

    @Override
    public void save(User user) {
        URL url;
        HttpURLConnection conn;
        try {
            url = new URI(baseUrl + "/signup").toURL();
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            throw new RuntimeException(e);
        }
        try{
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println("Error opening connection");
            throw new RuntimeException(e);
        }
        // NEW! configure for sending data from local to server
        conn.setDoOutput(true);
        try{
            conn.setRequestMethod("POST");
        }
        catch (ProtocolException e) {
            System.out.println("Protocol error");
            throw new RuntimeException(e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        final JSONObject body = new JSONObject();
        body.put("user_name", user.getName());
        body.put("email", user.getEmail());
        body.put("password", user.getPassword());

        try{
            final OutputStream os = conn.getOutputStream();
            os.write(body.toString().getBytes());
            os.flush();

            conn.getInputStream();
        }
        catch (IOException e) {
            System.out.println("Error opening connection");
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(String userName, String password) {
        URL url;
        try {
            url = new URI(baseUrl + "/login").toURL();
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            throw new RuntimeException(e);
        }

        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println("Error opening connection");
            throw new RuntimeException(e);
        }

        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            System.out.println("Protocol error");
            throw new RuntimeException(e);
        }
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

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
        } catch (IOException e) {
            System.out.println("Error opening connection: " + e.getMessage());
            throw new RuntimeException(e);
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
            url = new URI(baseUrl + "/exists").toURL();
        } catch (URISyntaxException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
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
        } catch (IOException e) {
            System.out.println("Error opening connection: " + e.getMessage());
            throw new RuntimeException(e);
        }


        final JSONObject res = new JSONObject(sb.toString());

        return res.getBoolean("exists");
    }
}