package use_case.recipe_search;

public interface RecipeSearchOutputBoundary {
    void prepareSuccessView(RecipeSearchOutputData outputData);
    void prepareFailView(RecipeSearchOutputData outputData);
}
