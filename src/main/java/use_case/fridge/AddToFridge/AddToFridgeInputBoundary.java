package use_case.fridge.AddToFridge;


public interface AddToFridgeInputBoundary{
    /**
     * Executes the add-to-fridge-list use case.
     *
     * @param requestModel the input data containing the user and recipe information.
     */
    AddToFridgeResponseModel addIngredient(AddToFridgeRequestModel requestModel) throws Exception;
}

