package cookinglist;

import use_case.cookinglist.AddToCookingListOutputBoundary;
import use_case.cookinglist.AddToCookingListOutputData;

/**
 * Fake presenter for testing AddToCookingListInteractor.
 * Captures the last output data for verification in tests.
 */
public class FakeAddToCookingListPresenter implements AddToCookingListOutputBoundary {
    private AddToCookingListOutputData lastOutput;

    @Override
    public void present(AddToCookingListOutputData outputData) {
        this.lastOutput = outputData;
    }

    /**
     * Gets the last output data presented.
     * @return the last output data
     */
    public AddToCookingListOutputData getLastOutput() {
        return lastOutput;
    }
}
