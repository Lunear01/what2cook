package use_case.recipe_search;

/**
 * Input Boundary for the "search recipes by ingredients" use case.
 * Called by the controller.
 */
public interface RecipeSearchInputBoundary {

    /**
     * Execute the use case.
     *
     * @param inputData the input data containing ingredient names, etc.
     */
    void searchByIngredients(RecipeSearchInputData inputData);
}
