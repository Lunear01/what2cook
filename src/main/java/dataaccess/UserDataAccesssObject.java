package dataaccess;
import app.cookinglist.UserDataAccessInterface;
import entity.User;
import java.util.HashMap;
import java.util.Map;


public class UserDataAccesssObject {
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
