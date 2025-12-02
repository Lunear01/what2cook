package interface_adapter.fridgemodify;

import java.util.List;

import entity.Ingredient;
import interface_adapter.login.LoginViewModel;
import use_case.fridge.AddToFridge.AddToFridgeInputBoundary;
import use_case.fridge.AddToFridge.AddToFridgeRequestModel;
import use_case.fridge.DeleteFridge.DeleteFridgeInputBoundary;
import use_case.fridge.DeleteFridge.DeleteFridgeRequestModel;
import use_case.fridge.GetFridge.GetFridgeInputBoundary;
import use_case.fridge.GetFridge.GetFridgeRequestModel;
import use_case.fridge.GetFridge.GetFridgeResponseModel;

public class FridgeController {

    private final AddToFridgeInputBoundary addUc;
    private final GetFridgeInputBoundary getUc;
    private final DeleteFridgeInputBoundary deleteUc;
    private final LoginViewModel loginVm;
    private final FridgeViewModel viewModel;

    public FridgeController(
            AddToFridgeInputBoundary addUc,
            GetFridgeInputBoundary getUc,
            DeleteFridgeInputBoundary deleteUc,
            LoginViewModel loginVm,
            FridgeViewModel viewModel
    ) {
        this.addUc = addUc;
        this.getUc = getUc;
        this.deleteUc = deleteUc;
        this.loginVm = loginVm;
        this.viewModel = viewModel;
    }

    /**
     *  Add ingredient to fridge.
     *  update fridge ViewModel after adding.
     *  @param ingredientName the name of the ingredient to add
     *  @throws Exception if add fails
     */
    public void addIngredient(String ingredientName) throws Exception {
        final String username = loginVm.getState().getUsername();
        addUc.addIngredient(new AddToFridgeRequestModel(username, ingredientName));
        getIngredient();
    }

    /**
     * Get ingredients from fridge.
     * Update fridge ViewModel after getting.
     * @throws Exception if get fails
     */
    public void getIngredient() throws Exception {
        final String username = loginVm.getState().getUsername();
        final GetFridgeResponseModel response = getUc.getIngredient(new GetFridgeRequestModel(username));
        viewModel.setState(responseToFridgeState(response));
    }

    /**
     * Delete ingredients from fridge.
     * Update fridge ViewModel after getting.
     * @param ingredient the ingredient to delete
     * @throws Exception if delete fails
     */
    public void deleteIngredient(Ingredient ingredient) throws Exception {
        deleteUc.deleteIngredient(new DeleteFridgeRequestModel(
                loginVm.getState().getUsername(),
                ingredient.getIngredientId()
        ));
        getIngredient();
    }

    /**
     * Change GetFridgeResponseModel to FridgeState.
     * @param response the GetFridgeResponseModel from use case
     * @return the FridgeState for ViewModel
     */
    private FridgeState responseToFridgeState(GetFridgeResponseModel response) {
        final FridgeState state = new FridgeState();
        final List<Ingredient> ingredients = response.getIngredients();
        state.setIngredients(ingredients);
        return state;
    }
}
