package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

public class SignupController {

    private final SignupInputBoundary signupInteractor;

    public SignupController(SignupInputBoundary signupInteractor) {
        this.signupInteractor = signupInteractor;
    }

    public void signup(String username, String email, String password, String confirmPassword) {
        SignupInputData data = new SignupInputData(username, email, password, confirmPassword);
        signupInteractor.execute(data);
    }
}
