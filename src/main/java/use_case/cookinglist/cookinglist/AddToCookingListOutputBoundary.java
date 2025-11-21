package use_case.cookinglist.cookinglist;

public interface AddToCookingListOutputBoundary {
    void prepareSuccessView(AddToCookingListOutputData data);
    void prepareFailView(String errorMessage);
}
