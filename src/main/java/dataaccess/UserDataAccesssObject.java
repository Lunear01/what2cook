package dataaccess;
import app.cookinglist.UserDataAccessInterface;
import app.login.LoginUserDataAccessInterface;
import app.signup.SignupUserDataAccessInterface;
import entity.User;
import java.util.HashMap;
import java.util.Map;


public class UserDataAccesssObject implements UserDataAccessInterface, LoginUserDataAccessInterface, SignupUserDataAccessInterface {
    private final Map<String, User> users = new HashMap<>();

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
