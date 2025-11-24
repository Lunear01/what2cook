package use_case.cookinglist;

public interface AddToCookingListOutputBoundary {
    /**
     * Presents the output data for the add-to-cooking-list use case.
     *
     * @param outputData the output data containing the result of the use case.
     */
    void present(AddToCookingListOutputData outputData);
}
