package use_case.add_favorite_list;

public interface AddFavoriteRecipeInputBoundary {
    /**
     * Adds a recipe to the user's favorite list.
     *
     * @param inputData the data required to add the recipe
     */
    void execute(AddFavoriteRecipeInputData inputData);

    /**
     * Removes a recipe from the user's favorite list.
     *
     * @param inputData the data required to remove the recipe
     */
    void remove(AddFavoriteRecipeInputData inputData);
}
