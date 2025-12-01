package dataaccess;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * In-memory implementation of user data access.
 * Stores users in a simple map.
 */
public class InMemoryUserDataAccess implements
        LoginUserDataAccessInterface, SignupUserDataAccessInterface {

    // username -> User
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User get(String username, String password) {
        final User user = users.get(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    @Override
    public void save(User user) {
        if (user != null && user.getName() != null) {
            users.put(user.getName(), user);
        }
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }
}
