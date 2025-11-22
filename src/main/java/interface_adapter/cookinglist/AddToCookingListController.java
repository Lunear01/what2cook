package interface_adapter.cookinglist;

import entity.Recipe;
import use_case.cookinglist.AddToCookingListInputBoundary;
import use_case.cookinglist.AddToCookingListInputData;

public class AddToCookingListController {
    private final AddToCookingListInputBoundary interactor;

    public AddToCookingListController(AddToCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void add(String username, Recipe recipe) {
        final AddToCookingListInputData inputData =
                new AddToCookingListInputData(username, recipe);
        interactor.execute(inputData);
    }
}
