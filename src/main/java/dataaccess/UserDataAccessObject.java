package dataaccess;
import entity.User;
import java.util.HashMap;
import java.util.Map;
import app.cookinglist.UserDataAccessInterface;

public class UserDataAccessObject implements UserDataAccessInterface {
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