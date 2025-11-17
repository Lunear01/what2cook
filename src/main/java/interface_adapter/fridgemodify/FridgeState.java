package interface_adapter.fridgemodify;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the fridge view.
 */
public class FridgeState {

    private List<String> myFridge = new ArrayList<>();

    public FridgeState() {
    }

    public FridgeState(List<String> ingredients) {
        this.myFridge = new ArrayList<>(ingredients);
    }

    public List<String> getIngredients() {
        return new ArrayList<>(myFridge);
    }

    public void setIngredients(List<String> ingredients) {
        this.myFridge = new ArrayList<>(ingredients);
    }
}
