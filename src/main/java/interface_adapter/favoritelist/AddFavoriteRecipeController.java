package interface_adapter.favoritelist;

import entity.Recipe;
import use_case.add_favorite_list.AddFavoriteRecipeInputBoundary;
import use_case.add_favorite_list.AddFavoriteRecipeInputData;
import use_case.add_favorite_list.AddFavoriteRecipeInteractor;

public class AddFavoriteRecipeController {

    private final AddFavoriteRecipeInputBoundary interactor;

    public AddFavoriteRecipeController(AddFavoriteRecipeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void add(String username, Recipe recipe) {
        final AddFavoriteRecipeInputData inputData =
                new AddFavoriteRecipeInputData(username, recipe);
        interactor.execute(inputData);
    }

    /**
     * Add and get a user-friendly message
     * (either "already in favorites" or "added to favorites").
     * 这个专门给 RecipeSearchController 用。
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
