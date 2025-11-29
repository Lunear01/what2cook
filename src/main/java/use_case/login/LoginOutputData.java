package use_case.login;

/**
 * Data object containing the result of a successful login attempt.
 * Includes the username and a success flag.
 */
public class LoginOutputData {
    private final String username;
    private final boolean success;

    /**
     * Constructor for the LoginOutputData object.
     * @param username the username of the logged-in user
     * @param success whether the login attempt was successful
     */
    public LoginOutputData(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    /**
     * Getter for the username.
     * @return the username of the logged-in user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the success flag.
     * @return true if the login attempt was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
}

