package dataaccess;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.cookinglist.UserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class UserDataAccesssObject implements UserDataAccessInterface,
        LoginUserDataAccessInterface, SignupUserDataAccessInterface {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }
}
