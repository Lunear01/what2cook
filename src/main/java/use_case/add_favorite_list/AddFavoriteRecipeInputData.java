package use_case.add_favorite_list;

import entity.Recipe;

public class AddFavoriteRecipeInputData {

    private final String username;
    private final Recipe recipe;

    public AddFavoriteRecipeInputData(String username, Recipe recipe) {
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
