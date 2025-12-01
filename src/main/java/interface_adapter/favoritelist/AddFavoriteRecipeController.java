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
     * Adds the recipe and returns a user-friendly message.
     * @param username the name of the user whose favorites are updated
     * @param recipe   the recipe to add to the favorites
     * @return a user-friendly status message such as
     *         "already in favorites" or "added to favorites"
     */
    public String addAndGetMessage(String username, Recipe recipe) {
        final AddFavoriteRecipeInputData inputData =
                new AddFavoriteRecipeInputData(username, recipe);
        interactor.execute(inputData);

        String message = "";
        if (interactor instanceof AddFavoriteRecipeInteractor) {
            final AddFavoriteRecipeInteractor concrete =
                    (AddFavoriteRecipeInteractor) interactor;
            message = concrete.getLastMessage();
        }
        return message;
    }
}

