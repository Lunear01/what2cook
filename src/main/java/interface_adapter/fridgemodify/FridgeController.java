package interface_adapter.fridgemodify;

import entity.Ingredient;
import interface_adapter.login.LoginViewModel;
import use_case.fridge.AddToFridge.AddToFridgeInputBoundary;
import use_case.fridge.AddToFridge.AddToFridgeRequestModel;
import use_case.fridge.DeleteFridge.DeleteFridgeInputBoundary;
import use_case.fridge.DeleteFridge.DeleteFridgeRequestModel;
import use_case.fridge.GetFridge.GetFridgeInputBoundary;
import use_case.fridge.GetFridge.GetFridgeRequestModel;

/**
 * The Controller for fridge-related actions.
 */
public class FridgeController {

    private final AddToFridgeInputBoundary addUc;
    private final GetFridgeInputBoundary getUc;
    private final DeleteFridgeInputBoundary deleteUc;
    private final LoginViewModel loginVm;

    public FridgeController(AddToFridgeInputBoundary addUc, GetFridgeInputBoundary getUc,
                            DeleteFridgeInputBoundary deleteUc, LoginViewModel loginVm) {
        this.addUc = addUc;
        this.getUc = getUc;
        this.deleteUc = deleteUc;
        this.loginVm = loginVm;
    }

    /**
     * Adds an ingredient and triggers UI update.
     *
     * @param ingredient the name of the ingredient to add.
     */
    public void addIngredient(Ingredient ingredient) throws Exception {
        String username = loginVm.getUsername();

        AddToFridgeRequestModel request = new AddToFridgeRequestModel(username, ingredient.getIngredientId(),ingredient.getName());
        addUc.addIngredient(request);
    }

    /**
     * Delete an ingredient and triggers UI update.
     *
     * @param ingredient the ingredient to delete.
     */
    public void deleteIngredient(Ingredient ingredient) throws Exception {
        String username = loginVm.getUsername();

        DeleteFridgeRequestModel request = new DeleteFridgeRequestModel(username, ingredient.getIngredientId());
        deleteUc.deleteIngredient(request);
    }

    /**
     * Get the ingredients and triggers UI update.
     *
     *
     */
    public void GetIngredient() throws Exception {
        String username = loginVm.getUsername();

        GetFridgeRequestModel request = new GetFridgeRequestModel(username);
        getUc.getIngredient(request);
    }

//    /**
//     * Refreshes the ingredient list.
//     */
//    public void refresh() {
//        getUc.execute();
//    }
}
