package use_case.add_favorite_list;

import java.util.List;

import entity.Recipe;

public class AddFavoriteRecipeInteractor implements AddFavoriteRecipeInputBoundary {

    private final AddFavoriteRecipeDataAccessInterface favoritesDao;
    private final AddFavoriteRecipeOutputBoundary presenter;
    private String lastMessage = "";

    public AddFavoriteRecipeInteractor(AddFavoriteRecipeDataAccessInterface favoritesDao,
                                       AddFavoriteRecipeOutputBoundary presenter) {
        this.favoritesDao = favoritesDao;
        this.presenter = presenter;

    }

    public String getLastMessage() {
        return lastMessage;
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
            // 不存在：先加入 DAO，再重新取一遍列表
            favoritesDao.addToFavorites(username, recipe);
            updatedFavorites = favoritesDao.getFavorites(username);
            message = recipe.getTitle() + " added to your favorites!";
        }

        // 记录这次 message，留给 controller 用
        lastMessage = message;

        presenter.present(
                new AddFavoriteRecipeOutputData(
                        updatedFavorites,
                        message
                )
        );
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
