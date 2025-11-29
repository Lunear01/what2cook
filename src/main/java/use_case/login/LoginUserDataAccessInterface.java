package use_case.login;

import entity.User;

public interface LoginUserDataAccessInterface {
    /**
     * Returns the user with the given username, or {@code null} if no such user exists.
     *
     * @param username the username of the user to retrieve
     * @param password the password of the user to retrieve
     * @return the user with the given username, or {@code null} if not found
     */
    User get(String username, String password);

    /**
     * Saves the given user to the underlying data store.
     *
     * @param user the user to save
     */
    void save(User user);
}

