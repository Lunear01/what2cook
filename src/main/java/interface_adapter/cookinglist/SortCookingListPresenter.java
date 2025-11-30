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
        // Bug #8 修复: 添加 null 检查
        if (outputData.getMessage() != null) {
            viewModel.setStatusMessage(outputData.getMessage());
        }
    }
}
