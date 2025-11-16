package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

import entity.Recipe;

public class RecipeSearchState {

    private List<String> ingredients = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private String error;

    public RecipeSearchState() {

    }

    // Copy constructor (用于 ViewModel 更新时不共享引用)
    public RecipeSearchState(RecipeSearchState copy) {
        this.ingredients = new ArrayList<>(copy.ingredients);
        this.recipes = new ArrayList<>(copy.recipes);
        this.error = copy.error;
    }

    // ====== Getters ======
    public List<String> getIngredients() {
        return ingredients;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getError() {
        return error;
    }

    // ====== Setters ======
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setError(String error) {
        this.error = error;
    }
}

