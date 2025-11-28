package use_case.recipe_search;

/**
 * Output Boundary for the "search recipes by ingredients" use case.
 * Implemented by the presenter.
 */
public interface RecipeSearchOutputBoundary {

    /**
     * Present the results of the use case.
     *
     * @param outputData the result data (recipes + error info).
     */
    void present(RecipeSearchOutputData outputData);
}
