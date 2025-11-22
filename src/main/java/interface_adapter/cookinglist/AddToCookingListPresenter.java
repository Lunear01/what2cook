package interface_adapter.cookinglist;


import use_case.cookinglist.AddToCookingListOutputBoundary;
import use_case.cookinglist.AddToCookingListOutputData;



public class AddToCookingListPresenter implements AddToCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public AddToCookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddToCookingListOutputData outputData) {
        if (outputData.getUpdatedCookingList() != null) {
            viewModel.setPersonalCookingList(outputData.getUpdatedCookingList());
        }
        viewModel.setStatusMessage(outputData.getMessage());
    }
}