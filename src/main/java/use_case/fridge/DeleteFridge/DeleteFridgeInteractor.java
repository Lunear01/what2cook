package use_case.fridge.DeleteFridge;

import dataaccess.IngredientDataAccess;

public class DeleteFridgeInteractor implements DeleteFridgeInputBoundary {
    private final IngredientDataAccess fridgeDataAccess;
    private final DeleteFridgeOutputBoundary presenter;

    public DeleteFridgeInteractor(IngredientDataAccess fridgeDataAccess,
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
