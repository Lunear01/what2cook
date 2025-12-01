package interface_adapter.cookinglist;

import use_case.cookinglist.GetCookingList.GetCookingListOutputBoundary;
import use_case.cookinglist.GetCookingList.GetCookingListOutputData;

/**
 * Presenter for getting the cooking list.
 */
public class GetCookingListPresenter implements GetCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public GetCookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
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
}
