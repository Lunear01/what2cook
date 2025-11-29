package dataaccess;

import entity.User;

public interface UserDataAccess {
    /**
     * Sign up the user.
     * @param user the User
     */
    void save(User user) throws Exception;

    /**
     * Login the user.
     * @param user_name user's name
     * @param password user's password
     */
    User get(String user_name, String password) throws Exception;

    /**
     * Check if the user exists.
     * @param user_name user's name
     */
    boolean exists(String user_name) throws Exception;
}
