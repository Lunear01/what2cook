package interface_adapter.favoritelist;

import entity.Recipe;
import use_case.add_favorite_list.AddFavoriteRecipeInputBoundary;
import use_case.add_favorite_list.AddFavoriteRecipeInputData;

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
}
