package use_case.signup;

/**
 * Input Boundary for the Signup Use Case.
 *
 */
public interface SignupInputBoundary {

    /**
     * Executes the Signup Use Case.
     * @param inputData the SignupInputData Object containing the user's signup information.
     */
    void execute(SignupInputData inputData);
}
