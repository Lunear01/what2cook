package interface_adapter.signup;

public class SignupState {
    private String username = "";
    private String password = "";
    private String confirmPassword = "";
    private String errorMessage = "";
    private boolean created = false;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean isCreated() { return created; }
    public void setCreated(boolean created) { this.created = created; }
}
