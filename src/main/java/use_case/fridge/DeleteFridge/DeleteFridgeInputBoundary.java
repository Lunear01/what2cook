package use_case.fridge.DeleteFridge;


public interface DeleteFridgeInputBoundary {
    DeleteFridgeResponseModel deleteIngredient(DeleteFridgeRequestModel requestModel) throws Exception;
}
