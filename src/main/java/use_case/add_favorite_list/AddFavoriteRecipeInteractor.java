package use_case.add_favorite_list;

import java.util.List;

import entity.Recipe;

public class AddFavoriteRecipeInteractor implements AddFavoriteRecipeInputBoundary {

    private final AddFavoriteRecipeDataAccessInterface favoritesDao;
    private final AddFavoriteRecipeOutputBoundary presenter;

    /**
     * Stores the message for the last add attempt,
     * so that callers (e.g., controllers) can retrieve it.
     */
    private String lastMessage;

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
        final String message;

        if (exists) {
            message = recipe.getTitle() + " is already in your favorites.";
            outputData = new AddFavoriteRecipeOutputData(
                    currentFavorites,
                    message
            );
        }
        else {
            favoritesDao.addToFavorites(username, recipe);
            final List<Recipe> updated = favoritesDao.getFavorites(username);

            message = recipe.getTitle() + " added to your favorites!";
            outputData = new AddFavoriteRecipeOutputData(
                    updated,
                    message
            );
        }

        lastMessage = message;

        presenter.present(outputData);
    }

    /**
     * Returns the message of the last add attempt.
     *
     * @return last status message, or null if execute() has not been called yet
     */
    public String getLastMessage() {
        return lastMessage;
    }
}
