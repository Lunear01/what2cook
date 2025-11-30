package interface_adapter.cookinglist;

import use_case.cookinglist.SortCookingList.SortCookingListInputBoundary;
import use_case.cookinglist.SortCookingList.SortCookingListInputData;
import use_case.cookinglist.SortCookingList.SortCookingListInputData.SortType;

/**
 * Controller for sorting the cooking list.
 */
public class SortCookingListController {
    private final SortCookingListInputBoundary interactor;

    public SortCookingListController(SortCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Sort the cooking list for the specified user.
     *
     * @param username the username.
     * @param sortType the sort type (by health score or calories).
     */
    public void sort(String username, SortType sortType) {
        final SortCookingListInputData inputData =
                new SortCookingListInputData(username, sortType);
        interactor.execute(inputData);
    }
}
