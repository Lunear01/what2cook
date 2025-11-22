package use_case.cookinglist;

import entity.User;

public interface UserDataAccessInterface {
    User get(String username);

    void save(User user);
}
