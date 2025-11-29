package use_case.login;

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

        String errorMessage = null;
        LoginOutputData outputData = null;

        final String username = inputData.getUsername();
        final String password = inputData.getPassword();

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            errorMessage = "Username cannot be empty";
        }
        else if (password == null || password.trim().isEmpty()) {
            errorMessage = "Password cannot be empty";
        }
        else {
            // Get user
            final User user = userDataAccess.get(username, password);

            if (user == null) {
                errorMessage = "User not found";
            }
            else if (!user.getPassword().equals(password)) {
                errorMessage = "Incorrect password";
            }
            else {
                // Success
                outputData = new LoginOutputData(username, true);
            }
        }

        // Now exactly ONE return path
        if (errorMessage != null) {
            presenter.presentFailure(errorMessage);
        }
        else {
            presenter.presentSuccess(outputData);
        }
    }
}

