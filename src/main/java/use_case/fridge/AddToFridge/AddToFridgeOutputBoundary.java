package use_case.fridge.AddToFridge;

public interface AddToFridgeOutputBoundary {

    AddToFridgeResponseModel prepareSuccessView(AddToFridgeResponseModel responseModel);

    AddToFridgeResponseModel prepareFailView(String errorMessage);

}
