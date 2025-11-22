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

    public void setCurrentIngredients(List<Ingredient> ingredients) {
        final RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setIngredients(new ArrayList<>(ingredients));
        setState(newState);
        firePropertyChanged();
    }

    public void setRecipes(List<Recipe> recipes) {
        final RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setRecipes(new ArrayList<>(recipes));
        setState(newState);
        firePropertyChanged();
    }

    public void setError(String error) {
        final RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setError(error);
        setState(newState);
        firePropertyChanged();
    }
}
