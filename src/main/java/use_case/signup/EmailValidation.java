package use_case.signup;

/**
 * Email Validation Services.
 * Checks and returns a boolean on whether the email is valid.
 */
public interface EmailValidation {
    boolean isValid(String email);
}
