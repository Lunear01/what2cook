package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary interactor;

    public SignupController(SignupInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void signup(String username, String email, String password, String confirmPassword) {
        final SignupInputData input = new SignupInputData(username, email, password, confirmPassword);
        interactor.execute(input);
    }
}
