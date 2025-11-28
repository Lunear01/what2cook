package interface_adapter.recipe_search;

import use_case.recipe_search.RecipeSearchOutputBoundary;
import use_case.recipe_search.RecipeSearchOutputData;

public class RecipeSearchPresenter implements RecipeSearchOutputBoundary {

    private final RecipeSearchViewModel viewModel;

    public RecipeSearchPresenter(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RecipeSearchOutputData outputData) {
        final RecipeSearchState newState =
                new RecipeSearchState(viewModel.getState());

        newState.setIngredients(outputData.getIngredients());
        newState.setRecipes(outputData.getRecipes());
        newState.setError(null);

        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(RecipeSearchOutputData outputData) {
        final RecipeSearchState newState =
                new RecipeSearchState(viewModel.getState());

        newState.setIngredients(outputData.getIngredients());
        newState.setRecipes(outputData.getRecipes());
        newState.setError(outputData.getErrorMessage());

        viewModel.setState(newState);
        viewModel.firePropertyChanged();
    }
}
