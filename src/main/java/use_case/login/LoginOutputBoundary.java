package use_case.login;

/**
 * Output Boundary for the Login Use Case.
 */
public interface LoginOutputBoundary {
    /**
     * Presents the successful login result to the presenter.
     *
     * @param outputData the data containing information about the successful login
     */
    void presentSuccess(LoginOutputData outputData);

    /**
     * Presents the failed login result to the presenter.
     *
     * @param errorMessage the message describing why the login failed
     */
    void presentFailure(String errorMessage);
}

