package use_case.recipe_search;

import java.util.List;

import entity.Ingredient;

public class RecipeSearchInputData {

    private final List<Ingredient> ingredients;

    public RecipeSearchInputData(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
