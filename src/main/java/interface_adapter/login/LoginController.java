package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * Controller for the Login use case.
 */
public class LoginController {
    private final LoginInputBoundary interactor;

    public LoginController(LoginInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Attempts to log in a user with the given credentials.
     * Wraps the input into a {@code LoginInputData} object and
     * passes it to the interactor.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     */
    public void login(String username, String password) {
        final LoginInputData inputData = new LoginInputData(username, password);
        interactor.execute(inputData);
    }
}

