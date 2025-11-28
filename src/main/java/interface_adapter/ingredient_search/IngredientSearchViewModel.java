package interface_adapter.ingredient_search;

import interface_adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Ingredient Search view.
 * Holds the list of ingredient names (as strings) and any error message.
 */
public class IngredientSearchViewModel extends ViewModel<IngredientSearchState> {

    public static final String VIEW_NAME = "ingredient search";

    public IngredientSearchViewModel() {
        super(VIEW_NAME);
        setState(new IngredientSearchState());
    }

    /**
     * Helper to update ingredients in the state.
     */
    public void setIngredients(List<String> ingredients) {
        IngredientSearchState newState = new IngredientSearchState(getState());
        newState.setIngredients(new ArrayList<>(ingredients));
        setState(newState); // 这里会通过基类 ViewModel 触发 PropertyChange
    }

    /**
     * Helper to update error message in the state.
     */
    public void setError(String error) {
        IngredientSearchState newState = new IngredientSearchState(getState());
        newState.setError(error);
        setState(newState);
    }
}
