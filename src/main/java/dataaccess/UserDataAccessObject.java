package dataaccess;

import entity.User;
import use_case.cookinglist.UserDataAccessInterface;
import app.login.LoginUserDataAccessInterface;
import app.signup.SignupUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class UserDataAccessObject implements
        UserDataAccessInterface,
        LoginUserDataAccessInterface,
        SignupUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    public UserDataAccessObject() {
        // 如果你想保留一个默认用户，用 Builder 创建
        User defaultUser = new User.UserBuilder()
                .setName("Csc207")
                .setPassword("password")
                .build();
        users.put(defaultUser.getName(), defaultUser);
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }
}
