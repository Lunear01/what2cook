package interface_adapter.cookinglist;

import entity.Recipe;
import use_case.cookinglist.AddToCookingList.AddToCookingListInputBoundary;
import use_case.cookinglist.AddToCookingList.AddToCookingListInputData;
import use_case.cookinglist.GetCookingList.GetCookingListInputBoundary;
import use_case.cookinglist.GetCookingList.GetCookingListInputData;
import use_case.cookinglist.SortCookingList.SortCookingListInputBoundary;
import use_case.cookinglist.SortCookingList.SortCookingListInputData;
import use_case.cookinglist.SortCookingList.SortCookingListInputData.SortType;

/**
 * Unified controller for cooking list operations (add, get, sort).
 * Follows the same pattern as FridgeController.
 */
public class CookingListController {

    private final AddToCookingListInputBoundary addInteractor;
    private final GetCookingListInputBoundary getInteractor;
    private final SortCookingListInputBoundary sortInteractor;

    public CookingListController(
            AddToCookingListInputBoundary addInteractor,
            GetCookingListInputBoundary getInteractor,
            SortCookingListInputBoundary sortInteractor
    ) {
        this.addInteractor = addInteractor;
        this.getInteractor = getInteractor;
        this.sortInteractor = sortInteractor;
    }

    /**
     * Adds the given recipe to the specified user's cooking list.
     *
     * @param username the name of the user.
     * @param recipe   the recipe to add.
     */
    public void addRecipe(String username, Recipe recipe) {
        final AddToCookingListInputData inputData =
                new AddToCookingListInputData(username, recipe);
        addInteractor.execute(inputData);
    }

    /**
     * Gets the cooking list for the specified user.
     *
     * @param username the name of the user.
     */
    public void getCookingList(String username) {
        final GetCookingListInputData inputData = new GetCookingListInputData(username);
        getInteractor.execute(inputData);
    }

    /**
     * Sorts the cooking list for the specified user.
     *
     * @param username the username.
     * @param sortType the sort type (by health score or calories).
     */
    public void sortCookingList(String username, SortType sortType) {
        final SortCookingListInputData inputData =
                new SortCookingListInputData(username, sortType);
        sortInteractor.execute(inputData);
    }
}
