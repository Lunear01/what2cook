package use_case.ingredient_search;

/**
 * Input data for the ingredient search / add use case.
 * Carries the current user name and the ingredient to add (if any).
 */
public class IngredientSearchInputData {

    private final String userName;
    private final String ingredientName;

    public IngredientSearchInputData(String userName, String ingredientName) {
        this.userName = userName;
        this.ingredientName = ingredientName;
    }

    public String getUserName() {
        return userName;
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
