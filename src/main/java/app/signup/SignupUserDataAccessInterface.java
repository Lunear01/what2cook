package app.signup;

import entity.User;

public interface SignupUserDataAccessInterface {
    boolean existsByUsername(String username);
    void saveUser(User user);
}
