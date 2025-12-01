package interface_adapter.fridgemodify;

import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import use_case.fridge.AddToFridge.AddToFridgeOutputBoundary;
import use_case.fridge.AddToFridge.AddToFridgeResponseModel;
import use_case.fridge.DeleteFridge.DeleteFridgeOutputBoundary;
import use_case.fridge.DeleteFridge.DeleteFridgeResponseModel;
import use_case.fridge.GetFridge.GetFridgeOutputBoundary;
import use_case.fridge.GetFridge.GetFridgeResponseModel;

public class FridgePresenter implements
        AddToFridgeOutputBoundary,
        DeleteFridgeOutputBoundary,
        GetFridgeOutputBoundary {

    private final FridgeViewModel viewModel;

    public FridgePresenter(FridgeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public AddToFridgeResponseModel prepareSuccessView(AddToFridgeResponseModel responseModel) {

        final FridgeState state = viewModel.getState();
        state.setErrorMessage("");

        final List<Ingredient> updated = new ArrayList<>(state.getIngredients());

        // Construct new Ingredient
        final Ingredient newIngredient = Ingredient.builder()
                .setName(responseModel.getIngredientName())
                .setId(responseModel.getIngredientId())
                .build();

        updated.add(newIngredient);
        state.setIngredients(updated);

        viewModel.setState(state);
        return responseModel;
    }

    // ========== SUCCESS: Delete from fridge ==========
    @Override
    public DeleteFridgeResponseModel prepareSuccessView(DeleteFridgeResponseModel responseModel) {

        final FridgeState state = viewModel.getState();
        state.setErrorMessage("");

        final List<Ingredient> updated = new ArrayList<>(state.getIngredients());

        // Remove ingredient by ID
        updated.removeIf(ing -> ing.getIngredientId() == responseModel.getIngredientId());

        state.setIngredients(updated);
        viewModel.setState(state);

        return responseModel;
    }

    // ========== SUCCESS: Get fridge ==========
    @Override
    public GetFridgeResponseModel prepareSuccessView(GetFridgeResponseModel responseModel) {

        final FridgeState state = viewModel.getState();
        state.setErrorMessage("");

        // Directly store the Ingredient list
        state.setIngredients(responseModel.getIngredients());

        viewModel.setState(state);
        return responseModel;
    }

    // ========== FAIL (shared) ==========
    @Override
    public AddToFridgeResponseModel prepareFailView(String errorMessage) {
        setError(errorMessage);
        return null;
    }

    @Override
    public DeleteFridgeResponseModel prepareFailViewDelete(String errorMessage) {
        setError(errorMessage);
        return null;
    }

    @Override
    public GetFridgeResponseModel prepareFailViewGet(String errorMessage) {
        setError(errorMessage);
        return null;
    }

    // Helper for all failure cases
    private void setError(String errorMessage) {
        final FridgeState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
    }
}
