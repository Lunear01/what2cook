package interface_adapter.recipe_search;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;
import interface_adapter.ViewModel;

public class RecipeSearchViewModel extends ViewModel<RecipeSearchState> {

    public static final String VIEW_NAME = "recipe search";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public RecipeSearchViewModel() {
        super(VIEW_NAME);
        setState(new RecipeSearchState());
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, getState());
    }

    /**
     * Updates the current ingredient list in the view model state and
     * notifies listeners that the state has changed.
     *
     * @param ingredients the list of current ingredients.
     */
    public void setCurrentIngredients(List<Ingredient> ingredients) {
        final RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setIngredients(new ArrayList<>(ingredients));
        setState(newState);
        firePropertyChanged();
    }

    /**
     * Updates the list of recipes in the view model state and
     * notifies listeners that the state has changed.
     *
     * @param recipes the list of recipes to set.
     */
    public void setRecipes(List<Recipe> recipes) {
        final RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setRecipes(new ArrayList<>(recipes));
        setState(newState);
        firePropertyChanged();
    }

    /**
     * Sets the error message in the view model state and
     * notifies listeners that the state has changed.
     *
     * @param error the error message to set.
     */
    public void setError(String error) {
        final RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setError(error);
        setState(newState);
        firePropertyChanged();
    }
}
