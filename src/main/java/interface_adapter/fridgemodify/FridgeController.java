package interface_adapter.fridgemodify;

import use_case.AddIngredientUseCase;
import use_case.GetIngredientUseCase;

/**
 * The Controller for fridge-related actions.
 */
public class FridgeController {

    private final AddIngredientUseCase addUC;
    private final GetIngredientUseCase getUC;

    public FridgeController(AddIngredientUseCase addUC, GetIngredientUseCase getUC) {
        this.addUC = addUC;
        this.getUC = getUC;
    }

    /**
     * Adds an ingredient and triggers UI update.
     */
    public void addIngredient(String name) {
        addUC.addToList(name);
        getUC.execute();
    }

    /**
     * Refreshes the ingredient list.
     */
    public void refresh() {
        getUC.execute();
    }
}
