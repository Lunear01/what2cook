package interface_adapter.cookinglist;

import use_case.cookinglist.RemoveFromCookingListInputBoundary;
import use_case.cookinglist.RemoveFromCookingListInputData;
import entity.Recipe;

/**
 * UI -> Remove use case controller.
 * 和 AddToCookingListController 对称。
 */
public class RemoveFromCookingListController {

    private final RemoveFromCookingListInputBoundary interactor;

    public RemoveFromCookingListController(RemoveFromCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void remove(String username, Recipe recipe) {
        RemoveFromCookingListInputData inputData =
                new RemoveFromCookingListInputData(username, recipe);
        interactor.execute(inputData);
    }
}
