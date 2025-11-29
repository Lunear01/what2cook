package use_case.fridge.GetFridge;

import dataaccess.IngredientDataAccess;
import entity.Ingredient;

import java.util.List;

public class GetFridgeInteractor implements GetFridgeInputBoundary{
    private final IngredientDataAccess fridgeDataAccess;
    private final GetFridgeOutputBoundary presenter;

    public GetFridgeInteractor(IngredientDataAccess fridgeDataAccess,
                                 GetFridgeOutputBoundary presenter) {
        this.fridgeDataAccess = fridgeDataAccess;
        this.presenter = presenter;
    }

    @Override
    public GetFridgeResponseModel getIngredient(GetFridgeRequestModel requestModel) throws Exception {

        List<Ingredient> ingredients = fridgeDataAccess.getAllIngredients(requestModel.getUserName());

        GetFridgeResponseModel response = new GetFridgeResponseModel(
                requestModel.getUserName(),
                ingredients
        );

        return presenter.prepareSuccessView(response);
    }
}

