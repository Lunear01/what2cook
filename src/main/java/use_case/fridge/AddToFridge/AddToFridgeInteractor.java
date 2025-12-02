package use_case.fridge.AddToFridge;

import use_case.fridge.IngredientDataAccessInterface;

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

        final int id = fridgeDataAccess.addIngredient(requestModel.getUserName(), requestModel.getIngredientName());

        final AddToFridgeResponseModel response = new AddToFridgeResponseModel(
                requestModel.getUserName(),
                id,
                requestModel.getIngredientName()
        );

        return presenter.prepareSuccessView(response);
    }
}
