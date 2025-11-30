package use_case.signup;

import java.util.regex.Pattern;

/**
 * Password Validation Service implementation.
 * Used to check the validity of user passwords.
 */
public class PasswordValidationService implements PasswordValidation {

    private static final Pattern STRONG_PASSWORD =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");

    /**
     * Password Validation method.
     * Returns True if the password matches the STRONG_PASSWORD regex pattern, False otherwise.
     * The STRONG_PASSWORD pattern matches a string if it contains at least 8 characters and contains at least
     * 1 uppercase, 1 lowercase, 1 number and 1 special character.
     * @param password the password to be validated.
     */
    @Override
    public boolean isStrong(String password) {
        return password != null && STRONG_PASSWORD.matcher(password).matches();
    }
}
