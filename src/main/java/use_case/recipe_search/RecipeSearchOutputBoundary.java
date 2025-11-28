package use_case.recipe_search;

public interface RecipeSearchOutputBoundary {
    /**
     * Prepares the success view for the recipe search use case.
     *
     * @param outputData the output data containing found recipes and ingredient list
     */
    void prepareSuccessView(RecipeSearchOutputData outputData);

    /**
     * Prepares the failure view for the recipe search use case.
     *
     * @param outputData the output data containing the error message
     */
    void prepareFailView(RecipeSearchOutputData outputData);
}
