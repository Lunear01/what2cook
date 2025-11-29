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

        // 1. 当前收藏列表 / current favorites
        final List<Recipe> currentFavorites = favoritesDao.getFavorites(username);

        // 2. 判断是否已经存在 / check duplicate
        final boolean exists = currentFavorites.stream()
                .anyMatch(recipeE -> recipeE.getId() == recipe.getId());

        if (exists) {
            presenter.present(
                    new AddFavoriteRecipeOutputData(
                            currentFavorites,
                            recipe.getTitle() + " is already in your favorites."
                    )
            );
            return;
        }

        // 3. 加入收藏 / add to favorites
        favoritesDao.addToFavorites(username, recipe);

        // 4. 重新读取更新后的列表 / reload updated list
        final List<Recipe> updated = favoritesDao.getFavorites(username);

        presenter.present(
                new AddFavoriteRecipeOutputData(
                        updated,
                        recipe.getTitle() + " added to your favorites!"
                )
        );
    }
}
