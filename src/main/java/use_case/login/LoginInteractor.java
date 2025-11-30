package use_case.login;

import entity.User;

/**
 * Interactor for the Login use case.
 * Authenticates a user by username and password.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary presenter;

    /**
     * Constructor for the LoginInteractor.
     * @param userDataAccess the data access object for the user table
     * @param presenter the presenter for the Login use case.
     */
    public LoginInteractor(LoginUserDataAccessInterface userDataAccess, LoginOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes the Login use case.
     * @param inputData the input data containing the user's login credentials.
     */
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
            // Get user from the backend
            final User user = userDataAccess.get(username, password);

            if (!userDataAccess.existsByName(username)) {
                errorMessage = "User not found";
            }
            else if (user == null) {
                errorMessage = "Wrong username or password";
            }
            else {
                // Success
                outputData = new LoginOutputData(username, true);
            }
        }

        // Now we have exactly ONE return path.
        if (errorMessage != null) {
            presenter.presentFailure(errorMessage);
        }
        else {
            presenter.presentSuccess(outputData);
        }
    }
}

