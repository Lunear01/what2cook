package interface_adapter.cookinglist;

import use_case.cookinglist.GetCookingList.GetCookingListInputBoundary;
import use_case.cookinglist.GetCookingList.GetCookingListInputData;

/**
 * Controller for getting the cooking list.
 */
public class GetCookingListController {
    private final GetCookingListInputBoundary interactor;

    public GetCookingListController(GetCookingListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Gets the cooking list for the specified user.
     * This method constructs the input data and delegates execution to the interactor.
     *
     * @param username the name of the user.
     */
    public void getCookingList(String username) {
        final GetCookingListInputData inputData = new GetCookingListInputData(username);
        interactor.execute(inputData);
    }
}
