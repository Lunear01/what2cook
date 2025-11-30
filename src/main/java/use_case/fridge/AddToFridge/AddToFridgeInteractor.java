package use_case.fridge.AddToFridge;

import dataaccess.IngredientDataAccessInterface;

public class AddToFridgeInteractor implements AddToFridgeInputBoundary {

    private final IngredientDataAccessInterface fridgeDataAccess;
    private final AddToFridgeOutputBoundary presenter;

    public AddToFridgeInteractor(IngredientDataAccessInterface fridgeDataAccess,
                                 AddToFridgeOutputBoundary presenter) {
        this.fridgeDataAccess = fridgeDataAccess;
        this.presenter = presenter;
    }

    @Override
    public AddToFridgeResponseModel addIngredient(AddToFridgeRequestModel requestModel) throws Exception {

        fridgeDataAccess.addIngredient(requestModel.getUserName(), requestModel.getIngredientId(),
                requestModel.getIngredientName());

        final AddToFridgeResponseModel response = new AddToFridgeResponseModel(
                requestModel.getUserName(),
                requestModel.getIngredientId(),
                requestModel.getIngredientName()
        );

        return presenter.prepareSuccessView(response);
    }
}
