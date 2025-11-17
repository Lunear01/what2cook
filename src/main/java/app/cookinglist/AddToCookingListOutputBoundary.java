package app.cookinglist;

public interface AddToCookingListOutputBoundary {
    void prepareSuccessView(AddToCookingListOutputData data);
    void prepareFailView(String errorMessage);
}
