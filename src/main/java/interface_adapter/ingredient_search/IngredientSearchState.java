package interface_adapter.ingredient_search;

import java.util.ArrayList;
import java.util.List;

public class IngredientSearchState {

    private List<String> ingredients = new ArrayList<>();
    private String currentInput = "";
    private String error;

    public IngredientSearchState() {

    }

    // copy constructor
    public IngredientSearchState(IngredientSearchState copy) {
        this.ingredients = new ArrayList<>(copy.ingredients);
        this.currentInput = copy.currentInput;
        this.error = copy.error;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void setCurrentInput(String currentInput) {
        this.currentInput = currentInput;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
