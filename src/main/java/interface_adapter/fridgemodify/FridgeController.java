package interface_adapter.fridgemodify;

import use_case.fridge.AddIngredientUseCase;
import use_case.fridge.DeleteIngredientUseCase;
import use_case.fridge.GetIngredientUseCase;

/**
 * The Controller for fridge-related actions.
 */
public class FridgeController {

    private final AddIngredientUseCase addUc;
    private final GetIngredientUseCase getUc;
    private final DeleteIngredientUseCase deleteUc;

    public FridgeController(AddIngredientUseCase addUc, GetIngredientUseCase getUc, DeleteIngredientUseCase deleteUc) {
        this.addUc = addUc;
        this.getUc = getUc;
        this.deleteUc = deleteUc;
    }

    /**
     * Adds an ingredient and triggers UI update.
     *
     * @param name the name of the ingredient to add.
     */
    public void addIngredient(String name) {
        addUc.addToList(name);
        getUc.execute();
    }

    /**
     * Delete an ingredient and triggers UI update.
     *
     * @param name the name of the ingredient to delete.
     */
    public void deleteIngredient(String name) {
        deleteUc.deleteToList(name);
    }

    /**
     * Refreshes the ingredient list.
     */
    public void refresh() {
        getUc.execute();
    }
}
