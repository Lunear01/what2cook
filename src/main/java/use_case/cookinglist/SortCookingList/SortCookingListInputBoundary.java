package use_case.cookinglist.SortCookingList;

/**
 * Input boundary interface for sorting the cooking list.
 */
public interface SortCookingListInputBoundary {
    /**
     * Execute the sort cooking list use case.
     * @param inputData the input data containing sort parameters
     */
    void execute(SortCookingListInputData inputData);
}
