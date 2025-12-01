package use_case.cookinglist.GetCookingList;

/**
 * Output boundary for getting the cooking list.
 */
public interface GetCookingListOutputBoundary {
    /**
     * Present the cooking list results.
     * @param outputData the output data containing the cooking list
     */
    void present(GetCookingListOutputData outputData);
}
