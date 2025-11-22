package use_case.login;

public interface LoginInputBoundary {
    /**
     * Executes the login use case with the given input data.
     *
     * @param inputData the input data containing the user's login credentials.
     */
    void execute(LoginInputData inputData);
}

