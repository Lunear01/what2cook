package use_case.signup;

import entity.User;

/**
 * Interactor for the Signup use case.
 *
 * 负责：
 *  - 做所有输入检查（是否为空、两次密码是否一致）
 *  - 调用 EmailValidation / PasswordValidation 做格式与强度检查
 *  - 调用 SignupUserDataAccessInterface 检查用户名是否已存在并保存用户
 *  - 把结果交给 SignupOutputBoundary（Presenter）
 */
public class SignupInteractor implements SignupInputBoundary {

    private final SignupUserDataAccessInterface userDataAccess;
    private final SignupOutputBoundary signupPresenter;
    private final EmailValidation emailValidation;
    private final PasswordValidation passwordValidation;

    /**
     * Creates a new SignupInteractor.
     *
     * @param userDataAccess   DAO for signup (检查是否存在 / 保存用户)
     * @param signupPresenter  presenter for output
     * @param emailValidation  email 验证服务
     * @param passwordValidation password 验证服务
     */
    public SignupInteractor(SignupUserDataAccessInterface userDataAccess,
                            SignupOutputBoundary signupPresenter,
                            EmailValidation emailValidation,
                            PasswordValidation passwordValidation) {
        this.userDataAccess = userDataAccess;
        this.signupPresenter = signupPresenter;
        this.emailValidation = emailValidation;
        this.passwordValidation = passwordValidation;
    }

    @Override
    public void execute(SignupInputData inputData) {

        final String username = inputData.getUsername();
        final String email = inputData.getEmail();
        final String password = inputData.getPassword();
        final String confirmPassword = inputData.getConfirmPassword();

        // ===== 1. 基础非空检查 =====
        if (username == null || username.trim().isEmpty()) {
            signupPresenter.presentFailure("Username must not be empty.");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            signupPresenter.presentFailure("Email must not be empty.");
            return;
        }

        if (password == null || password.isEmpty()) {
            signupPresenter.presentFailure("Password must not be empty.");
            return;
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            signupPresenter.presentFailure("Please confirm your password.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            signupPresenter.presentFailure("Passwords do not match.");
            return;
        }

        // ===== 2. Email 格式检查 =====
        if (!emailValidation.isValid(email)) {
            signupPresenter.presentFailure("Please enter a valid email address.");
            return;
        }

        // ===== 3. 密码强度检查 =====
        if (!passwordValidation.isStrong(password)) {
            signupPresenter.presentFailure("Password is too weak.");
            return;
        }

        // ===== 4. 用户名是否已存在 =====
        if (userDataAccess.existsByName(username)) {
            signupPresenter.presentFailure("Username already exists.");
            return;
        }

        // ===== 5. 创建 User 实体并保存 =====
        final User newUser = User.builder()
                .withName(username)
                .withEmail(email)
                .withPassword(password)
                .build();

        userDataAccess.save(newUser);

        // ===== 6. 通知 presenter 成功 =====
        final SignupOutputData outputData =
                new SignupOutputData(username, true);

        signupPresenter.presentSuccess(outputData);
    }
}
