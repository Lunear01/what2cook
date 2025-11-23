package use_case.signup;

public class SignupOutputData {
    private final String username;
    private final boolean created;

    public SignupOutputData(String username, boolean created) {
        this.username = username;
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public boolean isCreated() {
        return created;
    }
}
