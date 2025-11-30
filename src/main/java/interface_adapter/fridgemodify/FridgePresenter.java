package interface_adapter.fridgemodify;

import use_case.fridge.AddToFridge.AddToFridgeOutputBoundary;
import use_case.fridge.AddToFridge.AddToFridgeResponseModel;
import use_case.fridge.DeleteFridge.DeleteFridgeOutputBoundary;
import use_case.fridge.DeleteFridge.DeleteFridgeResponseModel;
import use_case.fridge.GetFridge.GetFridgeOutputBoundary;
import use_case.fridge.GetFridge.GetFridgeResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Unified presenter for Add / Delete / Get fridge use cases.
 */
public class FridgePresenter implements
        AddToFridgeOutputBoundary,
        DeleteFridgeOutputBoundary,
        GetFridgeOutputBoundary {

    private final FridgeViewModel viewModel;

    public FridgePresenter(FridgeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // ------------ SUCCESS: Add ------------
    @Override
    public AddToFridgeResponseModel prepareSuccessView(AddToFridgeResponseModel responseModel) {

        FridgeState state = viewModel.getState();
        state.setErrorMessage("");

        List<String> updated = new ArrayList<>(state.getIngredients());
        updated.add(responseModel.getIngredientName());

        state.setIngredients(updated);
        viewModel.setState(state);

        return responseModel;
    }

    // ------------ SUCCESS: Delete ------------
    @Override
    public DeleteFridgeResponseModel prepareSuccessView(DeleteFridgeResponseModel responseModel) {

        FridgeState state = viewModel.getState();
        state.setErrorMessage("");

        List<String> updated = new ArrayList<>(state.getIngredients());

        // the ingredientName is NOT known in delete response, so remove by ID index
        // For now: simply do updated.removeIf(...)
        updated.removeIf(name -> name.hashCode() == responseModel.getIngredientId());

        state.setIngredients(updated);
        viewModel.setState(state);

        return responseModel;
    }

    // ------------ SUCCESS: Get ------------
    @Override
    public GetFridgeResponseModel prepareSuccessView(GetFridgeResponseModel responseModel) {

        FridgeState state = viewModel.getState();
        state.setErrorMessage("");

        List<String> names = new ArrayList<>();
        responseModel.getIngredients().forEach(i -> names.add(i.getName()));

        state.setIngredients(names);
        viewModel.setState(state);

        return responseModel;
    }

    // ------------ FAIL (shared by all 3 interfaces) ------------
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

    // Helper to reduce code duplication
    private void setError(String errorMessage) {
        FridgeState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
    }
}
