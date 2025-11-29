package use_case.signup;

/**
 * Password Validation Services.
 * Used to check the validity of user passwords.
 */
public interface PasswordValidation {
    /**
     * Password Validation method.
     * Returns True if the password is valid, False otherwise.
     * @param password the password to be validated.
     */
    boolean isStrong(String password);
}
