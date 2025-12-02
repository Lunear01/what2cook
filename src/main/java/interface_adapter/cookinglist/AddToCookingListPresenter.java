package interface_adapter.cookinglist;

import use_case.cookinglist.AddToCookingList.AddToCookingListOutputBoundary;
import use_case.cookinglist.AddToCookingList.AddToCookingListOutputData;

public class AddToCookingListPresenter implements AddToCookingListOutputBoundary {

    private final CookingListViewModel viewModel;

    public AddToCookingListPresenter(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddToCookingListOutputData outputData) {
        System.out.println("\n*** Presenter: present() called ***");
        System.out.println("Presenter: Output data received");
        System.out.println("  - Message: " + outputData.getMessage());
        System.out.println("  - Updated list size: " +
            (outputData.getUpdatedCookingList() != null ?
             outputData.getUpdatedCookingList().size() : "null"));

        if (outputData.getUpdatedCookingList() != null) {
            System.out.println("Presenter: Updating viewModel with new cooking list");
            viewModel.setPersonalCookingList(outputData.getUpdatedCookingList());
            System.out.println("Presenter: viewModel.setPersonalCookingList() completed");
        }
        if (outputData.getMessage() != null) {
            System.out.println("Presenter: Setting status message: " + outputData.getMessage());
            viewModel.setStatusMessage(outputData.getMessage());
            System.out.println("Presenter: viewModel.setStatusMessage() completed");
        }

        System.out.println("*** Presenter: present() completed ***\n");
    }
}
