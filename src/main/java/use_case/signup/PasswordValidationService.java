package use_case.signup;

import java.util.regex.Pattern;

public class PasswordValidationService implements PasswordValidation {

    // At least 8 characters, 1 lowercase, 1 uppercase, 1 number, 1 special character
    private static final Pattern STRONG_PASSWORD =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");

    @Override
    public boolean isStrong(String password) {
        return password != null && STRONG_PASSWORD.matcher(password).matches();
    }
}
