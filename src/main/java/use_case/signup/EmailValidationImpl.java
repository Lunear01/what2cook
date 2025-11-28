package use_case.signup;

import java.util.regex.Pattern;

/**
 * Validates email format using a simple regex pattern.
 */
public class EmailValidationImpl implements EmailValidation {

    // Simple regex: something@something.something
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
