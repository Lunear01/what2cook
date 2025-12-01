package interface_adapter.ingredient_search;

import interface_adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the Ingredient Search screen.
 * Holds the list of ingredient names currently entered by the user.
 */
public class IngredientSearchViewModel extends ViewModel<IngredientSearchState> {

    public static final String VIEW_NAME = "ingredient search";

    public IngredientSearchViewModel() {
        super(VIEW_NAME);
        setState(new IngredientSearchState());
    }

    /**
     * Replace the whole ingredient list.
     */
    public void setIngredients(List<String> ingredients) {
        IngredientSearchState newState = new IngredientSearchState(getState());
        newState.setIngredients(new ArrayList<>(ingredients));
        // setState 会自动 fire PropertyChange
        setState(newState);
    }

    /**
     * Add a single ingredient name to the list and notify the view immediately.
     */
    public void addIngredient(String ingredientName) {
        if (ingredientName == null || ingredientName.isBlank()) {
            return;
        }

        IngredientSearchState current = getState();
        IngredientSearchState newState = new IngredientSearchState(current);
        newState.getIngredients().add(ingredientName.trim());

        setState(newState);
    }
}
