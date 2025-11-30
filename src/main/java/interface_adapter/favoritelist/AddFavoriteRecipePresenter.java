package interface_adapter.favoritelist;

import use_case.add_favorite_list.AddFavoriteRecipeOutputBoundary;
import use_case.add_favorite_list.AddFavoriteRecipeOutputData;

/**
 * Presenter for the "add favorite recipe" use case.
 * Maps output data to the FavoriteListViewModel.
 */
public class AddFavoriteRecipePresenter implements AddFavoriteRecipeOutputBoundary {

    private final FavoriteListViewModel viewModel;

    public AddFavoriteRecipePresenter(FavoriteListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddFavoriteRecipeOutputData outputData) {
        if (outputData.getFavorites() != null) {
            viewModel.setFavoriteList(outputData.getFavorites());
        }
        viewModel.setStatusMessage(outputData.getMessage());
    }
}
