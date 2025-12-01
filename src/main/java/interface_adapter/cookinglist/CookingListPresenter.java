package interface_adapter.cookinglist;

import use_case.cookinglist.AddToCookingList.AddToCookingListOutputBoundary;
import use_case.cookinglist.AddToCookingList.AddToCookingListOutputData;
import use_case.cookinglist.GetCookingList.GetCookingListOutputBoundary;
import use_case.cookinglist.GetCookingList.GetCookingListOutputData;
import use_case.cookinglist.SortCookingList.SortCookingListOutputBoundary;
import use_case.cookinglist.SortCookingList.SortCookingListOutputData;

/**
 * Unified presenter for cooking list operations (add, get, sort).
 * Implements all three output boundaries and updates the shared view model.
 */
public class CookingListPresenter implements
        AddToCookingListOutputBoundary,
        GetCookingListOutputBoundary,
        SortCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public CookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddToCookingListOutputData outputData) {
        if (outputData.getUpdatedCookingList() != null) {
            viewModel.setPersonalCookingList(outputData.getUpdatedCookingList());
        }
        if (outputData.getMessage() != null) {
            viewModel.setStatusMessage(outputData.getMessage());
        }
    }

    @Override
    public void present(GetCookingListOutputData outputData) {
        if (outputData.getCookingList() != null) {
            viewModel.setPersonalCookingList(outputData.getCookingList());
        }
        if (outputData.getMessage() != null) {
            viewModel.setStatusMessage(outputData.getMessage());
        }
    }

    @Override
    public void present(SortCookingListOutputData outputData) {
        if (outputData.getSortedCookingList() != null) {
            viewModel.setPersonalCookingList(outputData.getSortedCookingList());
        }
        if (outputData.getMessage() != null) {
            viewModel.setStatusMessage(outputData.getMessage());
        }
    }
}
