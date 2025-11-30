package use_case.add_favorite_list;

public interface AddFavoriteRecipeOutputBoundary {
    /**
     * Presents the result of adding or removing a favorite recipe.
     *
     * @param outputData the data to present
     */
    void present(AddFavoriteRecipeOutputData outputData);
}
