package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;

public class RecipeSearchState {

    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private String error;

    public RecipeSearchState() {}

    // copy constructor
    public RecipeSearchState(RecipeSearchState copy) {
        this.ingredients = new ArrayList<>(copy.ingredients);
        this.recipes = new ArrayList<>(copy.recipes);
        this.error = copy.error;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
