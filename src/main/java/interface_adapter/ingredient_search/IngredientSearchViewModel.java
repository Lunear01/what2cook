package interface_adapter.ingredient_search;

import interface_adapter.ViewModel;

public class IngredientSearchViewModel extends ViewModel<IngredientSearchState> {

    public static final String VIEW_NAME = "ingredient search";

    public IngredientSearchViewModel() {
        super(VIEW_NAME);
        setState(new IngredientSearchState());
    }
}
