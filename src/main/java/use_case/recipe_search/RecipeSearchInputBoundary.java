package use_case.recipe_search;

public interface RecipeSearchInputBoundary {
    /**
     * Executes the recipe search use case.
     *
     * @param inputData the input data containing the search query
     */
    void execute(RecipeSearchInputData inputData);
}
