package use_case.fridge.GetFridge;

public interface GetFridgeInputBoundary {
    GetFridgeResponseModel getIngredient(GetFridgeRequestModel requestModel) throws Exception;
}
