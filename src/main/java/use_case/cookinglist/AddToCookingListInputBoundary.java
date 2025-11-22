package use_case.cookinglist;

public interface AddToCookingListInputBoundary {
    /**
     * Executes the add-to-cooking-list use case.
     *
     * @param inputData the input data containing the user and recipe information.
     */
    void execute(AddToCookingListInputData inputData);
}
