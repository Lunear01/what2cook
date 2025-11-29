package interface_adapter.fridgemodify;

import entity.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the fridge view.
 */
public class FridgeState {

    private List<String> myFridge = new ArrayList<>();
    private String errorMessage = "";

    public FridgeState() {}

    public FridgeState(List<String> ingredients) {
        this.myFridge = new ArrayList<>(ingredients);
    }

        public List<String> getIngredients() {
        return new ArrayList<>(myFridge);
    }

    public void setIngredients(List<String> ingredients) {
        this.myFridge = new ArrayList<>(ingredients);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
