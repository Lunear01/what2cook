package use_case.add_favorite_list;

import java.util.List;

import entity.Recipe;

public class AddFavoriteRecipeInteractor implements AddFavoriteRecipeInputBoundary {

    private final AddFavoriteRecipeDataAccessInterface favoritesDao;
    private final AddFavoriteRecipeOutputBoundary presenter;

    /**
     * Stores the message for the last add attempt,
     * so that the controller can retrieve and show it.
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

        final List<Recipe> updatedFavorites;
        final String message;

        if (exists) {
            // 已经存在：不再 add，只用当前列表
            updatedFavorites = currentFavorites;
            message = recipe.getTitle() + " is already in your favorites.";
        }
        else {
            // 不存在：先加入 DAO，再重新取一次列表
            favoritesDao.addToFavorites(username, recipe);
            updatedFavorites = favoritesDao.getFavorites(username);
            message = recipe.getTitle() + " added to your favorites!";
        }

        lastMessage = message;

        presenter.present(
                new AddFavoriteRecipeOutputData(
                        updatedFavorites,
                        message
                )
        );
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
