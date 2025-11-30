package use_case.login;

import entity.User;

/**
 * Data Access Interface for the Login Use Case.
 * Used to retrieve and save users from the database
 */
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
     * Saves the given user to the backend database.
     *
     * @param user the user to save
     */
    void save(User user);

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username);
}

