package use_case.signup;

import entity.User;
import entity.UserBuilder;

/**
 * Interactor for the Signup use case.
 * Validates input, ensures username is unique, and saves the new user.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccess;
    private final SignupOutputBoundary presenter;

    public SignupInteractor(SignupUserDataAccessInterface userDataAccess, SignupOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SignupInputData inputData) {
        final String username = inputData.getUsername();
        final String email = inputData.getEmail();
        final String password = inputData.getPassword();
        final String confirm = inputData.getConfirmPassword();

        // Basic validations
        if (username == null || username.trim().isEmpty()) {
            presenter.presentFailure("Username cannot be empty");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            presenter.presentFailure("Password cannot be empty");
            return;
        }
        if (!password.equals(confirm)) {
            presenter.presentFailure("Passwords do not match");
            return;
        }

        // Check uniqueness
        if (userDataAccess.existsByName(username)) {
            presenter.presentFailure("Username already exists");
            return;
        }

        // Create and save user
        final User user = new UserBuilder()
                .withName(username)
                .withPassword(password)
                .withEmail(email)
                .build();
        userDataAccess.save(user);

        presenter.presentSuccess(new SignupOutputData(username, true));
    }
}
