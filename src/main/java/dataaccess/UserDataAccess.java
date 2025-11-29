package dataaccess;

import entity.User;

public interface UserDataAccess {
    /**
     * Sign up the user.
     * @param user_name user's name
     * @param email user's email
     * @param password user's password
     */
    void save(String user_name, String email, String password) throws Exception;

    /**
     * Login the user.
     * @param email user's email
     * @param password user's password
     */
    User get(String email, String password) throws Exception;

    boolean exists(String email) throws Exception;
}
