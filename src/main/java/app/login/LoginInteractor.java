package app.login;

import entity.User;

/**
 * Interactor for the Login use case.
 * Authenticates a user by username and password.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary presenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccess, LoginOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        String username = inputData.getUsername();
        String password = inputData.getPassword();

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            presenter.presentFailure("Username cannot be empty");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            presenter.presentFailure("Password cannot be empty");
            return;
        }

        // Get user from data access
        User user = userDataAccess.getUser(username);

        // Check if user exists and password matches
        if (user == null) {
            presenter.presentFailure("User not found");
            return;
        }

        if (!user.getPassword().equals(password)) {
            presenter.presentFailure("Incorrect password");
            return;
        }

        // Success
        LoginOutputData outputData = new LoginOutputData(username, true);
        presenter.presentSuccess(outputData);
    }
}

