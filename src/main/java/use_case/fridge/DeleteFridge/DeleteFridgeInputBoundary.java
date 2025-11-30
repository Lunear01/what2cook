package use_case.fridge.DeleteFridge;

public interface DeleteFridgeInputBoundary {
    /**
     * Deletes the specified ingredient from the fridge.
     *
     * @param requestModel the request containing the ingredient to delete
     * @return a response model describing the result
     */
    DeleteFridgeResponseModel deleteIngredient(DeleteFridgeRequestModel requestModel) throws Exception;
}
