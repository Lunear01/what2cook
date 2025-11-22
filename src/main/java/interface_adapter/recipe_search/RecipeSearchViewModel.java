package interface_adapter.recipe_search;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;

public class RecipeSearchViewModel extends ViewModel<RecipeSearchState> {

    public static final String VIEW_NAME = "recipe search";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public RecipeSearchViewModel() {
        super(VIEW_NAME);
        setState(new RecipeSearchState());
    }

    /** 添加监听器（UI 用）*/
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /** 通知 UI 更新*/
    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, getState());
    }

    /** 设置 ingredients（从 IngredientSearchView 传进来）*/
    public void setCurrentIngredients(List<Ingredient> ingredients) {
        RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setIngredients(new ArrayList<>(ingredients));
        setState(newState);
        firePropertyChanged();
    }

    /** 设置搜索出来的 recipes */
    public void setRecipes(List<Recipe> recipes) {
        RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setRecipes(new ArrayList<>(recipes));
        setState(newState);
        firePropertyChanged();
    }

    /** 设置错误信息 */
    public void setError(String error) {
        RecipeSearchState newState = new RecipeSearchState(getState());
        newState.setError(error);
        setState(newState);
        firePropertyChanged();
    }
}
