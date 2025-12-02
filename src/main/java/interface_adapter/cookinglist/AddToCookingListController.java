package interface_adapter.cookinglist;

import entity.Recipe;
import use_case.cookinglist.AddToCookingList.AddToCookingListInputBoundary;
import use_case.cookinglist.AddToCookingList.AddToCookingListInputData;

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
        System.out.println("DEBUG Controller: add() called");
        System.out.println("  - Username: " + username);
        System.out.println("  - Recipe: " + recipe.getTitle());

        final AddToCookingListInputData inputData =
                new AddToCookingListInputData(username, recipe);

        System.out.println("DEBUG Controller: Calling interactor.execute()...");
        interactor.execute(inputData);
        System.out.println("DEBUG Controller: interactor.execute() completed");
    }
}
