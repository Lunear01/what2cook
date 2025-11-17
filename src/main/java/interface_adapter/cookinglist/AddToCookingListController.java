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
        interactor.add(username, recipe);
    }
    public void remove(String username, Recipe recipe) {
        interactor.remove(username, recipe);
    }
}
