package interface_adapter.ingredient_search;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.ViewModel;

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
     * Replaces the full ingredient list with the provided one.
     *
     * @param ingredients the new list of ingredient names
     */
    public void setIngredients(final List<String> ingredients) {
        final IngredientSearchState newState =
                new IngredientSearchState(getState());

        newState.setIngredients(new ArrayList<>(ingredients));

        setState(newState);
    }

    /**
     * Adds a single ingredient to the current list.
     *
     * @param ingredientName the ingredient name to add
     */
    public void addIngredient(final String ingredientName) {
        if (ingredientName == null || ingredientName.isBlank()) {
            return;
        }

        final IngredientSearchState current = getState();
        final IngredientSearchState newState =
                new IngredientSearchState(current);

        newState.getIngredients().add(ingredientName.trim());

        setState(newState);
    }
}
