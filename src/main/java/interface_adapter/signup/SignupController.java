package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Controller for handling user signup actions.
 * Converts raw input from the UI into {@link SignupInputData} and
 * forwards it to the signup use case interactor.
 */
public class SignupController {

    private final SignupInputBoundary interactor;

    /**
     * Constructs a SignupController with the given interactor.
     *
     * @param interactor the signup input boundary implementation
     */
    public SignupController(final SignupInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Handles the signup request coming from the UI layer.
     *
     * @param username        the username entered by the user
     * @param email           the email entered by the user
     * @param password        the password entered by the user
     * @param confirmPassword the confirmation of the password
     */
    public void signup(
            final String username,
            final String email,
            final String password,
            final String confirmPassword
    ) {
        final SignupInputData input =
                new SignupInputData(username, email, password, confirmPassword);

        interactor.execute(input);
    }
}
