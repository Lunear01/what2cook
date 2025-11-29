package use_case.fridge.DeleteFridge;

public class DeleteFridgeRequestModel {
    private final String username;
    private final int ingredientId;

    public DeleteFridgeRequestModel(String username, int ingredientId) {
        this.username = username;
        this.ingredientId = ingredientId;
    }

    public String getUserName() {
        return username;
    }

    public int getIngredientId() {
        return ingredientId;
    }

}
