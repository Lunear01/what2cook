package use_case.fridge.DeleteFridge;


public interface DeleteFridgeOutputBoundary {
    DeleteFridgeResponseModel prepareSuccessView(DeleteFridgeResponseModel responseModel);

    DeleteFridgeResponseModel prepareFailViewDelete(String errorMessage);

}
