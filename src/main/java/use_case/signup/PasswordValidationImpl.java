package use_case.signup;

/**
 * Simple password validation:
 * - not null
 * - length at least 6
 */
public class PasswordValidationImpl implements PasswordValidation {

    @Override
    public boolean isStrong(String password) {
        if (password == null) {
            return false;
        }
        // 先写一个最简单的规则：长度 >= 6
        return password.length() >= 6;
    }
}
