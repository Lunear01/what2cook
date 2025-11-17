package dataaccess;
import app.cookinglist.UserDataAccessInterface;
import app.login.LoginUserDataAccessInterface;
import entity.User;
import java.util.HashMap;
import java.util.Map;


public class UserDataAccesssObject implements UserDataAccessInterface, LoginUserDataAccessInterface {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getName(), user);
    }
}
