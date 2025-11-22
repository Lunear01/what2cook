package interface_adapter.login;

import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * Presenter for the Login use case.
 */
public class LoginPresenter implements LoginOutputBoundary {
    private final LoginViewModel viewModel;

    public LoginPresenter(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(LoginOutputData outputData) {
        final LoginState state = new LoginState();
        state.setUsername(outputData.getUsername());
        state.setLoggedIn(true);
        state.setErrorMessage("");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void presentFailure(String errorMessage) {
        final LoginState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setLoggedIn(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}

