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

    /**
     * Adds the recipe and returns a user-friendly message
     * (either "already in favorites" or "added to favorites").
     * 专门给需要拿提示文案的调用方（例如 RecipeSearchController）用。
     */
    public String addAndGetMessage(String username, Recipe recipe) {
        final AddFavoriteRecipeInputData inputData =
                new AddFavoriteRecipeInputData(username, recipe);
        interactor.execute(inputData);

        if (interactor instanceof AddFavoriteRecipeInteractor) {
            final AddFavoriteRecipeInteractor concrete =
                    (AddFavoriteRecipeInteractor) interactor;
            return concrete.getLastMessage();
        }
        return "";
    }
}

