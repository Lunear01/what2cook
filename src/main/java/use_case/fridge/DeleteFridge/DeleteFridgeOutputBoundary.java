package use_case.fridge.DeleteFridge;

public interface DeleteFridgeOutputBoundary {
    /**
     * Prepares the success view for deleting an ingredient from the fridge.
     *
     * @param responseModel the response data for a successful deletion
     * @return the prepared response model
     */
    DeleteFridgeResponseModel prepareSuccessView(DeleteFridgeResponseModel responseModel);

    /**
     * Prepares the failure view for deleting an ingredient from the fridge.
     *
     * @param errorMessage the error message to present
     * @return a response model representing the failure
     */
    DeleteFridgeResponseModel prepareFailViewDelete(String errorMessage);

}
