package interface_adapter.cookinglist;


import use_case.cookinglist.cookinglist.AddToCookingListOutputBoundary;
import use_case.cookinglist.cookinglist.AddToCookingListOutputData;



public class AddToCookingListPresenter implements AddToCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public AddToCookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(AddToCookingListOutputData data) {
        // 直接用你封装好的方法
        viewModel.setPersonalCookingList(data.getPersonalCookingList());
        viewModel.setStatusMessage("Added to personal cooking list.");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setStatusMessage("Failed to add: " + errorMessage);
    }
}