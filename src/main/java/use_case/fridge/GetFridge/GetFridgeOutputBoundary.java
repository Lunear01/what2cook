package use_case.fridge.GetFridge;

public interface GetFridgeOutputBoundary {
    GetFridgeResponseModel prepareSuccessView(GetFridgeResponseModel responseModel);

    GetFridgeResponseModel prepareFailViewGet(String errorMessage);


}
