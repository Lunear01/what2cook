package interface_adapter.signup;

import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * Presenter for the signup use case.
 * Translates {@link SignupOutputData} into {@link SignupState} updates
 * on the {@link SignupViewModel}.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel viewModel;

    /**
     * Creates a presenter backed by the given view model.
     *
     * @param viewModel the view model to update
     */
    public SignupPresenter(final SignupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(final SignupOutputData outputData) {
        final SignupState state = new SignupState();
        state.setUsername(outputData.getUsername());
        state.setCreated(true);
        state.setErrorMessage("");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentFailure(final String errorMessage) {
        final SignupState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setCreated(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
