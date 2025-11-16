package interface_adapter.fridgemodify;

import java.util.List;

public class FridgeState {
    private List<String> myFridge;

    public void FridgeState(List<String> Fridge) {
        this.myFridge = Fridge;
    }

    public List<String> getIngredients() {
        return myFridge;
    }
}

