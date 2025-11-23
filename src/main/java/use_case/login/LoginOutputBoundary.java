package use_case.login;

public interface LoginOutputBoundary {
    void presentSuccess(LoginOutputData outputData);
    void presentFailure(String errorMessage);
}

