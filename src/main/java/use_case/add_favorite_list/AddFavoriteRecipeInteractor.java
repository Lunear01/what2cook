package use_case.add_favorite_list;

import java.util.List;

import entity.Recipe;

public class AddFavoriteRecipeInteractor implements AddFavoriteRecipeInputBoundary {

    private final AddFavoriteRecipeDataAccessInterface favoritesDao;
    private final AddFavoriteRecipeOutputBoundary presenter;

    public AddFavoriteRecipeInteractor(AddFavoriteRecipeDataAccessInterface favoritesDao,
                                       AddFavoriteRecipeOutputBoundary presenter) {
        this.favoritesDao = favoritesDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddFavoriteRecipeInputData inputData) {

        final String username = inputData.getUsername();
        final Recipe recipe = inputData.getRecipe();

        final List<Recipe> currentFavorites = favoritesDao.getFavorites(username);

        final boolean exists = currentFavorites.stream()
                .anyMatch(recipeE -> recipeE.getId() == recipe.getId());

        final AddFavoriteRecipeOutputData outputData;

        if (exists) {
            outputData = new AddFavoriteRecipeOutputData(
                    currentFavorites,
                    recipe.getTitle() + " is already in your favorites."
            );
        }
        else {
            favoritesDao.addToFavorites(username, recipe);
            final List<Recipe> updated = favoritesDao.getFavorites(username);

            outputData = new AddFavoriteRecipeOutputData(
                    updated,
                    recipe.getTitle() + " added to your favorites!"
            );
        }

        presenter.present(outputData);
    }

    @Override
    public void remove(AddFavoriteRecipeInputData inputData) {

        final String username = inputData.getUsername();
        final Recipe recipe = inputData.getRecipe();

        favoritesDao.removeFromFavorites(username, recipe);

        final List<Recipe> updated = favoritesDao.getFavorites(username);

        presenter.present(
                new AddFavoriteRecipeOutputData(
                        updated,
                        recipe.getTitle() + " removed from your favorites!"
                )
        );
    }
}
