package use_case.fridge.AddToFridge;

public class AddToFridgeRequestModel {
    private final String username;
    private final int ingredientId;
    private final String ingredientName;

    public AddToFridgeRequestModel(String username, int ingredientId, String ingredient_name) {
        this.username = username;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredient_name;
    }

    public String getUserName() {
        return username;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

}
