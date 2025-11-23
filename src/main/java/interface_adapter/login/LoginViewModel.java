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

    /**
     * Sets the username in the view model state and notifies listeners
     * that the username has changed.
     *
     * @param username the new username.
     */
    public void setUsername(String username) {
        final LoginState state = getState();
        state.setUsername(username);
        setState(state);
        firePropertyChanged("username");
    }

    public String getUsername() {
        return getState().getUsername();
    }

    /**
     * Sets the password in the view model state and notifies listeners
     * that the password has changed.
     *
     * @param password the new password.
     */
    public void setPassword(String password) {
        final LoginState state = getState();
        state.setPassword(password);
        setState(state);
        firePropertyChanged("password");
    }

    public String getPassword() {
        return getState().getPassword();
    }

    /**
     * Sets the error message in the view model state and notifies listeners
     * that the error message has changed.
     *
     * @param message the new error message.
     */
    public void setErrorMessage(String message) {
        final LoginState state = getState();
        state.setErrorMessage(message);
        setState(state);
        firePropertyChanged("error");
    }

    public String getErrorMessage() {
        return getState().getErrorMessage();
    }

    /**
     * Sets whether the user is logged in and notifies listeners
     * that the login status has changed.
     *
     * @param loggedIn {@code true} if the user is logged in, {@code false} otherwise.
     */
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

