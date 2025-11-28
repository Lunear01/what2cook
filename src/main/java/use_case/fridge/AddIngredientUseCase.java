package use_case.fridge;

import java.util.ArrayList;

/**
 * Add to frigde class.
 */
public class AddIngredientUseCase {
    private final ArrayList<String> frigde;

    public AddIngredientUseCase(ArrayList<String> fridge) {
        this.frigde = fridge;
    }

    /**
     * Add the new Ingredient to MyList.
     *
     * @param ingredient the new ingredient
     */
    public void addToList(String ingredient) {
        frigde.add(ingredient);
    }
}
