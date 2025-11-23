package interface_adapter.cookinglist;

import entity.Recipe;
import use_case.cookinglist.AddToCookingListInputBoundary;
import use_case.cookinglist.AddToCookingListInputData;

public class AddToCookingListController {
    private final AddToCookingListInputBoundary interactor;

    public AddToCookingListController(AddToCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Adds the given recipe to the specified user's cooking list.
     * This method constructs the input data and delegates execution to the interactor.
     *
     * @param username the name of the user.
     * @param recipe   the recipe to add.
     */
    public void add(String username, Recipe recipe) {
        final AddToCookingListInputData inputData =
                new AddToCookingListInputData(username, recipe);
        interactor.execute(inputData);
    }
}
