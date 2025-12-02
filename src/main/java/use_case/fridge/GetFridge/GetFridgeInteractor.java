package use_case.fridge.GetFridge;

import java.util.List;

import use_case.fridge.IngredientDataAccessInterface;
import entity.Ingredient;

public class GetFridgeInteractor implements GetFridgeInputBoundary {
    private final IngredientDataAccessInterface fridgeDataAccess;
    private final GetFridgeOutputBoundary presenter;

    public GetFridgeInteractor(IngredientDataAccessInterface fridgeDataAccess,
                               GetFridgeOutputBoundary presenter) {
        this.fridgeDataAccess = fridgeDataAccess;
        this.presenter = presenter;
    }

    @Override
    public GetFridgeResponseModel getIngredient(GetFridgeRequestModel requestModel) throws Exception {

        final List<Ingredient> ingredients = fridgeDataAccess.getAllIngredients(requestModel.getUserName());

        final GetFridgeResponseModel response = new GetFridgeResponseModel(
                requestModel.getUserName(),
                ingredients
        );

        return presenter.prepareSuccessView(response);
    }
}

