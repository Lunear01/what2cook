package use_case.add_favorite_list;

import java.util.List;

import entity.Recipe;

public interface AddFavoriteRecipeDataAccessInterface {

    /**
     * Returns the current favorites list for the given user.
     *
     * @param username the user's name.
     * @return the list of favorite recipes.
     */
    List<Recipe> getFavorites(String username);

    /**
     * Adds the given recipe to the user's favorites list.
     *
     * @param username the user's name.
     * @param recipe   the recipe to add.
     */
    void addToFavorites(String username, Recipe recipe);

    /**
     * Removes the given recipe from the user's favorites list.
     *
     * @param username the user's name.
     * @param recipe   the recipe to remove.
     */
    void removeFromFavorites(String username, Recipe recipe);
}
