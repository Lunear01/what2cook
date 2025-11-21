package use_case.cookinglist.cookinglist;
import entity.User;


public interface UserDataAccessInterface {
    User getUser(String username);
    void saveUser(User user);
}
