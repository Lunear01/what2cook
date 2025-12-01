package use_case.add_favorite_list;

import java.util.List;

import entity.Recipe;

/**
 * Interactor for adding recipes to the user's favorite list.
 * If the recipe is already in the list, it does not add it again
 * and simply returns the existing list with a message.
 */
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

        final AddFavoriteRecipeOutputData outputData;

        if (exists) {
            outputData = new AddFavoriteRecipeOutputData(
                    currentFavorites,
                    recipe.getTitle() + " is already in your favorites."
            );
        }
        else {
            // 不存在：先加入 DAO，再重新取一次列表
            favoritesDao.addToFavorites(username, recipe);
            final List<Recipe> updated = favoritesDao.getFavorites(username);

        // 记录本次 message，给 controller 用
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
