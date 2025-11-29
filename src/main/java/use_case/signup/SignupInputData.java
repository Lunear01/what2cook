package use_case.signup;

/**
 * Input Data Objects for the Signup Use Case.
 * Contains the user's signup information.
 */
public class SignupInputData {
    private final String username;
    private final String email;
    private final String password;
    private final String confirmPassword;

    /**
     * Constructor for the SignupInputData object.
     * @param username the username of the user to sign up
     * @param email the email of the user to sign up
     * @param password the password of the user to sign up
     * @param confirmPassword the password confirmation of the user to sign up
     */
    public SignupInputData(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    /**
     * Getter for the username.
     * @return the username of the user to sign up
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the email.
     * @return the email of the user to sign up
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the password.
     * @return the password of the user to sign up
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the password confirmation.
     * @return the password confirmation of the user to sign up
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
