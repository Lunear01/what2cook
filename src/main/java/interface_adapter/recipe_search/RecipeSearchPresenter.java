package interface_adapter.recipe_search;

import use_case.recipe_search.RecipeSearchOutputBoundary;
import use_case.recipe_search.RecipeSearchOutputData;

/**
 * Presenter for the Recipe Search use case.
 * Uses RecipeSearchViewModel's helper methods so that
 * property change events are correctly fired.
 */
public class RecipeSearchPresenter implements RecipeSearchOutputBoundary {

    private final RecipeSearchViewModel viewModel;

    public RecipeSearchPresenter(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(RecipeSearchOutputData outputData) {

        // 更新 recipes 列表（会内部 setState + firePropertyChanged）
        viewModel.setRecipes(outputData.getRecipes());

        // 更新错误信息
        if (outputData.isUseCaseFailed()) {
            viewModel.setError(outputData.getErrorMessage());
        } else {
            viewModel.setError(null);
        }
    }
}
