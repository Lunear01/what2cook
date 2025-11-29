package use_case.fridge.DeleteFridge;

public class DeleteFridgeResponseModel {
    private final String userName;
    private final int ingredientId;

    public DeleteFridgeResponseModel(String userName, int ingredientId) {
        this.userName = userName;
        this.ingredientId = ingredientId;
    }

    public String getUserName() {
        return userName;
    }

    public int getIngredientId() {
        return ingredientId;
    }
}
