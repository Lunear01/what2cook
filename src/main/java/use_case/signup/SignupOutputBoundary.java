package use_case.signup;

public interface SignupOutputBoundary {
    /**
     * Presents the successful signup result to the presenter.
     *
     * @param outputData the data containing information about the successful signup
     */
    void presentSuccess(SignupOutputData outputData);

    /**
     * Presents the failed signup result to the presenter.
     *
     * @param errorMessage the message describing why the signup failed
     */
    void presentFailure(String errorMessage);
}
