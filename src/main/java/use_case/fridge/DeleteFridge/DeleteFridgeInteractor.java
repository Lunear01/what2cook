package use_case.fridge.DeleteFridge;

import use_case.fridge.IngredientDataAccessInterface;

public class DeleteFridgeInteractor implements DeleteFridgeInputBoundary {
    private final IngredientDataAccessInterface fridgeDataAccess;
    private final DeleteFridgeOutputBoundary presenter;

    public DeleteFridgeInteractor(IngredientDataAccessInterface fridgeDataAccess,
                                  DeleteFridgeOutputBoundary presenter) {
        this.fridgeDataAccess = fridgeDataAccess;
        this.presenter = presenter;
    }

    @Override
    public DeleteFridgeResponseModel deleteIngredient(DeleteFridgeRequestModel requestModel) throws Exception {

        fridgeDataAccess.deleteIngredient(requestModel.getUserName(), requestModel.getIngredientId());

        DeleteFridgeResponseModel response = new DeleteFridgeResponseModel(
                requestModel.getUserName(),
                requestModel.getIngredientId()
        );
        return presenter.prepareSuccessView(response);
    }
}
