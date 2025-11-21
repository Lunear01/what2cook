package app.signup;

public interface SignupOutputBoundary {
    void presentSuccess(SignupOutputData outputData);
    void presentFailure(String errorMessage);
}
