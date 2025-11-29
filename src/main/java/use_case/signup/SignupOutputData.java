package use_case.signup;

/**
 * Output Data Objects for the Signup Use Case.
 */
public class SignupOutputData {
    private final String username;
    private final boolean created;

    /**
     * Constructor for the SignupOutputData object.
     * @param username the username of the user that was created
     * @param created boolean indicating if the user was successfully created
     */
    public SignupOutputData(String username, boolean created) {
        this.username = username;
        this.created = created;
    }

    /**
     * Getter for the username.
     * @return the username of the user that was created
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the created boolean.
     * @return true if the user was successfully created, false otherwise
     */
    public boolean isCreated() {
        return created;
    }
}
