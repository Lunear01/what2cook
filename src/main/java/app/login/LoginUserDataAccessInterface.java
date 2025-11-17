package app.login;

import entity.User;

public interface LoginUserDataAccessInterface {
    User getUser(String username);
    void saveUser(User user);
}

