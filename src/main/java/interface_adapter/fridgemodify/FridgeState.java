package interface_adapter.fridgemodify;

import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;

/**
 * State for the fridge view.
 */
public class FridgeState {
    private List<Ingredient> ingredients;
    private String errorMessage;

    public FridgeState() {
        this.ingredients = new ArrayList<>();
        this.errorMessage = "";
    }

    public FridgeState(List<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
        this.errorMessage = "";
    }

    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    public void setIngredients(List<Ingredient> newList) {
        this.ingredients = new ArrayList<>(newList);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String msg) {
        this.errorMessage = msg;
    }
}

