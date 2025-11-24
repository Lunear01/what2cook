package use_case;

import java.util.List;

import entity.Ingredient;

public class GetIngredientUseCase {
    private final List<Ingredient> fridge;

    public GetIngredientUseCase(List<Ingredient> fridge) {
        this.fridge = fridge;
    }

    /**
     * Returns the current list of ingredients in the fridge.
     *
     * @return the list of ingredients
     */
    public List<Ingredient> execute() {
        return fridge;
    }
}

