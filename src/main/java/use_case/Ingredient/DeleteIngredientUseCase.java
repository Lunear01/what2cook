package use_case.Ingredient;

import java.util.ArrayList;

/**
 * Add to frigde class.
 */
public class DeleteIngredientUseCase {
    private final ArrayList<String> frigde;

    public DeleteIngredientUseCase(ArrayList<String> fridge) {
        this.frigde = fridge;
    }

    /**
     * Delete the Ingredient to MyList
     * @param ingredient the new ingredient
     */
    public void deleteToList(String ingredient) {
        frigde.remove(ingredient);
    }
}
