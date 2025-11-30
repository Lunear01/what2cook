package interface_adapter.cookinglist;

import use_case.cookinglist.SortCookingListOutputBoundary;
import use_case.cookinglist.SortCookingListOutputData;

public class SortCookingListPresenter implements SortCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public SortCookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
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
