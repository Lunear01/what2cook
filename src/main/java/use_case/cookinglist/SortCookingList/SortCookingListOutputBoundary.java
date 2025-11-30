package use_case.cookinglist.SortCookingList;

/**
 * Output boundary interface for sorting the cooking list.
 */
public interface SortCookingListOutputBoundary {
    /**
     * Present the sorted cooking list.
     * @param outputData the output data containing sorted recipes and message
     */
    void present(SortCookingListOutputData outputData);
}
