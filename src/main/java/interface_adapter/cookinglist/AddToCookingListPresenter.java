package interface_adapter.cookinglist;


import app.cookinglist.AddToCookingListOutputBoundary;
import app.cookinglist.AddToCookingListOutputData;
import interface_adapter.note.ViewModel;


public class AddToCookingListPresenter {
    private final ViewModel viewModel;

    public AddToCookingListPresenter(ViewModel viewModel) {
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
