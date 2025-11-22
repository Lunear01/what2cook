package interface_adapter.signup;

import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary {
    private final SignupViewModel viewModel;

    public SignupPresenter(SignupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(SignupOutputData outputData) {
        SignupState state = new SignupState();
        state.setUsername(outputData.getUsername());
        state.setCreated(true);
        state.setErrorMessage("");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentFailure(String errorMessage) {
        SignupState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setCreated(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
