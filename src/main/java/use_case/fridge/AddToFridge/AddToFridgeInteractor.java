package use_case.fridge.AddToFridge;

import dataaccess.IngredientDataAccess;

public class AddToFridgeInteractor implements AddToFridgeInputBoundary {

    private final IngredientDataAccess fridgeDataAccess;
    private final AddToFridgeOutputBoundary presenter;

    public AddToFridgeInteractor(IngredientDataAccess fridgeDataAccess,
                                   AddToFridgeOutputBoundary presenter) {
        this.fridgeDataAccess = fridgeDataAccess;
        this.presenter = presenter;
    }

    @Override
    public AddToFridgeResponseModel addIngredient(AddToFridgeRequestModel requestModel) throws Exception {

        fridgeDataAccess.addIngredient(requestModel.getUserName(), requestModel.getIngredientId(),
                requestModel.getIngredientName());

        AddToFridgeResponseModel response = new AddToFridgeResponseModel(
                requestModel.getUserName(),
                requestModel.getIngredientId(),
                requestModel.getIngredientName()
        );

        return presenter.prepareSuccessView(response);
    }
}
