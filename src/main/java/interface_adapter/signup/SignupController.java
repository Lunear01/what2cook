package interface_adapter.signup;

import app.signup.SignupInputBoundary;
import app.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary interactor;

    public SignupController(SignupInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void signup(String username, String password, String confirmPassword) {
        final SignupInputData input = new SignupInputData(username, password, confirmPassword);
        interactor.execute(input);
    }
}
