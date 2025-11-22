package use_case.signup;

public interface SignupOutputBoundary {
    void presentSuccess(SignupOutputData outputData);
    void presentFailure(String errorMessage);
}
