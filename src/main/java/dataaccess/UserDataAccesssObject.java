package dataaccess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import entity.User;
import org.json.JSONObject;
import use_case.cookinglist.UserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class UserDataAccesssObject implements UserDataAccessInterface,
        LoginUserDataAccessInterface, SignupUserDataAccessInterface {

    private final String baseUrl = "http://localhost:3000/user";

    @Override
    public void save(User user) throws Exception {
        final URL url = new URL(baseUrl + "/signup");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", user.getName());
        body.put("email", user.getEmail());
        body.put("password", user.getPassword());

        final OutputStream os = conn.getOutputStream();
        os.write(body.toString().getBytes());
        os.flush();

        conn.getInputStream();
    }

    @Override
    public User get(String userName, String password) throws Exception {
        final URL url = new URL(baseUrl + "/login");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("password", password);

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

        final JSONObject userJson = res.getJSONObject("user");

        return User.builder()
                .withName(userJson.getString("user_name"))
                .withEmail(userJson.getString("email"))
                .withPassword(userJson.getString("password"))
                .build();
    }

    @Override
    public boolean exists(String userName) throws Exception {
        final URL url = new URL(baseUrl + "/exists");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
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

        return res.getBoolean("exists");
    }
}