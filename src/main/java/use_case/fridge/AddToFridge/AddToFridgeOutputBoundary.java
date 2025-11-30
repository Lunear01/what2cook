package use_case.fridge.AddToFridge;

public interface AddToFridgeOutputBoundary {
    /**
     * Prepares the success view for adding an ingredient to the fridge.
     *
     * @param responseModel the data to present on success
     * @return the prepared response model
     */
    AddToFridgeResponseModel prepareSuccessView(AddToFridgeResponseModel responseModel);

    /**
     * Prepares the failure view for adding an ingredient to the fridge.
     *
     * @param errorMessage the error message to present
     * @return the prepared response model containing failure information
     */
    AddToFridgeResponseModel prepareFailView(String errorMessage);
}
