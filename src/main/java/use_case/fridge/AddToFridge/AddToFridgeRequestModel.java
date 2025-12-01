package use_case.fridge.AddToFridge;

public class AddToFridgeRequestModel {
    private final String username;
    private final String ingredientName;

    public AddToFridgeRequestModel(String username, String ingredient_name) {
        this.username = username;
        this.ingredientName = ingredient_name;
    }

    public String getUserName() {
        return username;
    }

    public String getIngredientName() {
        return ingredientName;
    }

}
