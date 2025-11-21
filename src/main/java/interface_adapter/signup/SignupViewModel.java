package interface_adapter.signup;

import interface_adapter.ViewModel;

public class SignupViewModel extends ViewModel<SignupState> {
    public static final String VIEW_NAME = "signup";

    public SignupViewModel() {
        super(VIEW_NAME);
        this.setState(new SignupState());
    }
}
