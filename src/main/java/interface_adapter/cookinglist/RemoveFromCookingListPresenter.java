package interface_adapter.cookinglist;

import use_case.cookinglist.RemoveFromCookingListOutputBoundary;
import use_case.cookinglist.RemoveFromCookingListOutputData;

/**
 * Presenter for removing a recipe from the cooking list.
 * Updates CookingListViewModel.
 */
public class RemoveFromCookingListPresenter implements RemoveFromCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public RemoveFromCookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(RemoveFromCookingListOutputData outputData) {
        if (outputData.getUpdatedCookingList() != null) {
            viewModel.setPersonalCookingList(outputData.getUpdatedCookingList());
        }
        viewModel.setStatusMessage(outputData.getMessage());
    }
}
