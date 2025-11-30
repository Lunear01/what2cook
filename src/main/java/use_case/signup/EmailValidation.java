package use_case.signup;

/**
 * Email Validation Service interface.
 * Checks and returns a boolean on whether the email is valid.
 */
public interface EmailValidation {
    /**
     * Email Validation method.
     * Returns True if the email is valid, False otherwise.
     * @param email the email to be validated.
     */
    boolean isValid(String email);
}
