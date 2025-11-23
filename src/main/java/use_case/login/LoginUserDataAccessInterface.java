package use_case.login;

import entity.User;

public interface LoginUserDataAccessInterface {
    User get(String username);

    void save(User user);
}

