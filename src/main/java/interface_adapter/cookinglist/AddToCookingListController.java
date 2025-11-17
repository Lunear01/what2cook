package interface_adapter.cookinglist;


import app.cookinglist.AddToCookingListInputBoundary;
import app.cookinglist.AddToCookingListInputData;
import entity.Recipe;

public class AddToCookingListController {
    private final AddToCookingListInputBoundary interactor;

    public AddToCookingListController(AddToCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void add(String username, Recipe recipe) {
        AddToCookingListInputData inputData =
                new AddToCookingListInputData(username, recipe);
        interactor.execute(inputData);
    }
}
