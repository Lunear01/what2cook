package use_case.fridge.AddToFridge;

public class AddToFridgeResponseModel {
    private final String userName;
    private final String ingredientName;
    private final int ingredientId;

    public AddToFridgeResponseModel(String userName, int ingredientId, String ingredientName) {
        this.userName = userName;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
    }

    public String getUserName() {
        return userName;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public int getIngredientId() {
        return ingredientId;
    }
}
