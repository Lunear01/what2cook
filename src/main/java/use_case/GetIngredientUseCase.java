package use_case;

import entity.Ingredient;

import java.util.List;

public class GetIngredientUseCase {
    private final List<Ingredient> fridge;

    public GetIngredientUseCase(List<Ingredient> fridge) {
        this.fridge = fridge;
    }

    public List<Ingredient> execute() {
        return fridge;
    }
}

