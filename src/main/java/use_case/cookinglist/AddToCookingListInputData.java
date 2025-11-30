package use_case.cookinglist;

import entity.Recipe;

/**
 * Input data for adding a recipe to the cooking list.
 */
public class AddToCookingListInputData {

    private final String username;
    private final Recipe recipe;

    public AddToCookingListInputData(String username, Recipe recipe) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        this.username = username;
        this.recipe = recipe;
    }

    public String getUsername() {
        return username;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
