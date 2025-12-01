package use_case.fridge.GetFridge;

/**
 * Input boundary for the "Get Fridge" use case.
 * Defines the request format and the expected response type when retrieving
 * all ingredients stored in the user's fridge.
 */
public interface GetFridgeInputBoundary {

    /**
     * Retrieves the current list of ingredients in the fridge based on the
     * provided request model.
     *
     * @param requestModel the request containing any required retrieval parameters
     * @return a {@code GetFridgeResponseModel} containing the fridge's ingredients
     * @throws Exception if the retrieval process encounters an unexpected failure
     */
    GetFridgeResponseModel getIngredient(GetFridgeRequestModel requestModel) throws Exception;
}
