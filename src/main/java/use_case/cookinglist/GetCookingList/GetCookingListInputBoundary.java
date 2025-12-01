package use_case.cookinglist.GetCookingList;

/**
 * Input boundary for getting the cooking list.
 */
public interface GetCookingListInputBoundary {
    /**
     * Execute the get cooking list use case.
     * @param inputData the input data containing the username
     */
    void execute(GetCookingListInputData inputData);
}
