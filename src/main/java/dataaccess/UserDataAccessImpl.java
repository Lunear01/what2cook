package dataaccess;

import entity.User;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * User data access implementation that talks to the Node backend via HTTP,
 * and also keeps users in memory for the Login / Signup use cases.
 *
 * Implements:
 *  - UserDataAccess      (你原来的 HTTP 接口)
 *  - LoginUserDataAccessInterface
 *  - SignupUserDataAccessInterface
 */
public class UserDataAccessImpl implements UserDataAccess,
        LoginUserDataAccessInterface, SignupUserDataAccessInterface {

    private static final String BASE_URL = "http://localhost:3000/user";

    /** In-memory user store keyed by username. */
    private final Map<String, User> users = new HashMap<>();

    // =============== UserDataAccess (HTTP) ===============

    @Override
    public void signUp(String userName, String email, String password) throws Exception {
        // 调后端 /signup
        final URL url = new URL(BASE_URL + "/signup");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("email", email);
        body.put("password", password);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
            os.flush();
        }

        // 读一下响应，确保请求完成
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            while (br.readLine() != null) {
                // ignore body
            }
        }

        conn.disconnect();

        // 同步写入内存 map，这样 LoginInteractor 能找到
        final User user = User.builder()
                .withName(userName)
                .withEmail(email)
                .withPassword(password)
                .build();
        users.put(userName, user);
    }

    @Override
    public User login(String userName, String password) throws Exception {
        // 调后端 /login（主要给你以后用；目前 LoginInteractor 走的是 get() + 本地密码比对）
        final URL url = new URL(BASE_URL + "/login");
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        final JSONObject body = new JSONObject();
        body.put("user_name", userName);
        body.put("password", password);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes());
            os.flush();
        }

        final StringBuilder sb = new StringBuilder();
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }

        conn.disconnect();

        final JSONObject res = new JSONObject(sb.toString());
        final JSONObject userJson = res.getJSONObject("user");

        User user = User.builder()
                .withName(userJson.getString("user_name"))
                .withEmail(userJson.getString("email"))
                .withPassword(userJson.getString("password"))
                .build();

        // 也同步更新到本地 map
        users.put(user.getName(), user);

        return user;
    }

    // =============== SignupUserDataAccessInterface ===============

    @Override
    public boolean existsByName(String username) {
        // 只查本地 map（当前运行里是否已经注册过）
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        // 给 SignupInteractor 用：调用 HTTP signup，再放进本地 map

        try {
            signUp(user.getName(), user.getEmail(), user.getPassword());
        }
        catch (Exception e) {
            // 如果后端挂了，可以选择抛出 RuntimeException 或者只存在内存里
            // 这里简单打印一下，仍然把用户放进 map，保证前端流程能走通
            System.err.println("Warning: failed to sync signup to backend: " + e.getMessage());
            users.put(user.getName(), user);
        }
    }

    // =============== LoginUserDataAccessInterface ===============

    @Override
    public User get(String username) {
        // LoginInteractor 会:
        //   User user = userDataAccess.get(username);
        //   if (user == null) -> "User does not exist"
        //   else 比对密码
        return users.get(username);
    }
}
