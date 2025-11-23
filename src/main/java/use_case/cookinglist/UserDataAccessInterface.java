package use_case.cookinglist;

import entity.User;

public interface UserDataAccessInterface {
    /**
     * Retrieves a user by username.
     *
     * @param username the username of the user to retrieve.
     * @return the user with the given username, or {@code null} if not found.
     */
    User get(String username);

    /**
     * Saves the given user to the data store.
     *
     * @param user the user to save.
     */
    void save(User user);
}
