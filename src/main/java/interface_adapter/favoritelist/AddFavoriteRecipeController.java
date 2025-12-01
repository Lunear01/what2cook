package interface_adapter.favoritelist;

import entity.Recipe;
import use_case.add_favorite_list.AddFavoriteRecipeInputBoundary;
import use_case.add_favorite_list.AddFavoriteRecipeInputData;
import use_case.add_favorite_list.AddFavoriteRecipeInteractor;

/**
 * Controller for adding a recipe to the user's favorite list.
 * Constructs the input data and delegates execution to the interactor.
 */
public class AddFavoriteRecipeController {

    private final AddFavoriteRecipeInputBoundary interactor;

    public AddFavoriteRecipeController(AddFavoriteRecipeInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Adds the given recipe to the specified user's favorites list.
     *
     * @param username the name of the user.
     * @param recipe   the recipe to add as favorite.
     */
    public void add(String username, Recipe recipe) {
        final AddFavoriteRecipeInputData inputData =
                new AddFavoriteRecipeInputData(username, recipe);
        interactor.execute(inputData);
    }

    public String addAndGetMessage(String username, Recipe recipe) {
        final AddFavoriteRecipeInputData inputData =
                new AddFavoriteRecipeInputData(username, recipe);
        interactor.execute(inputData);

        // 这里做一次安全的 downcast 拿到 lastMessage
        if (interactor instanceof AddFavoriteRecipeInteractor) {
            final AddFavoriteRecipeInteractor concrete =
                    (AddFavoriteRecipeInteractor) interactor;
            return concrete.getLastMessage();
        }

        // 理论上不会走到这里，给个兜底
        return "";
    }

    /**
     * Removes the given recipe from the user's favorite list.
     *
     * @param username the user whose favorite list is modified
     * @param recipe   the recipe to remove
     */
    public void remove(String username, Recipe recipe) {
        final AddFavoriteRecipeInputData inputData =
                new AddFavoriteRecipeInputData(username, recipe);
        interactor.remove(inputData);
    }
}
