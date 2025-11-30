package use_case.login;

/**
 * Stores Input Data for the Login Use Case.
 */
public class LoginInputData {
    private final String username;
    private final String password;

    /**
     * Constructor for the LoginInputData object.
     * @param username the username of the user to log in
     * @param password the password of the user to log in
     */
    public LoginInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for the username.
     * @return the username of the user to log in
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the password.
     * @return the password of the user to log in
     */
    public String getPassword() {
        return password;
    }
}

