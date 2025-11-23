package use_case.cookinglist;

import entity.Recipe;

/**
 * Input data for removing a recipe.
 */
public class RemoveFromCookingListInputData {
    private final String username;
    private final Recipe recipe;

    public RemoveFromCookingListInputData(String username, Recipe recipe) {
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
