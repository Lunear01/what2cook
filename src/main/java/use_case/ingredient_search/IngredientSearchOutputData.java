package use_case.ingredient_search;

import java.util.List;

public class IngredientSearchOutputData {

    private final List<String> ingredientList;

    public IngredientSearchOutputData(List<String> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<String> getIngredientList() {
        return ingredientList;
    }
}
