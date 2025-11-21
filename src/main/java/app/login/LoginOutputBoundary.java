package app.login;

public interface LoginOutputBoundary {
    void presentSuccess(LoginOutputData outputData);
    void presentFailure(String errorMessage);
}

