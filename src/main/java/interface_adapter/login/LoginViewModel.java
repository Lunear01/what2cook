package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * ViewModel for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {
    public static final String VIEW_NAME = "login";

    public LoginViewModel() {
        super(VIEW_NAME);
        this.setState(new LoginState());
    }

    public void setUsername(String username) {
        final LoginState state = getState();
        state.setUsername(username);
        setState(state);
        firePropertyChanged("username");
    }

    public String getUsername() {
        return getState().getUsername();
    }

    public void setPassword(String password) {
        final LoginState state = getState();
        state.setPassword(password);
        setState(state);
        firePropertyChanged("password");
    }

    public String getPassword() {
        return getState().getPassword();
    }

    public void setErrorMessage(String message) {
        final LoginState state = getState();
        state.setErrorMessage(message);
        setState(state);
        firePropertyChanged("error");
    }

    public String getErrorMessage() {
        return getState().getErrorMessage();
    }

    public void setLoggedIn(boolean loggedIn) {
        final LoginState state = getState();
        state.setLoggedIn(loggedIn);
        setState(state);
        firePropertyChanged("loggedIn");
    }

    public boolean isLoggedIn() {
        return getState().isLoggedIn();
    }
}

