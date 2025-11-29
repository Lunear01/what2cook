package use_case.fridge.GetFridge;

public class GetFridgeRequestModel {
    private final String username;

    public GetFridgeRequestModel(String username) {
        this.username = username;
    }

    public String getUserName() {
        return username;
    }
}
