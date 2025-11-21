package interface_adapter.recipe_search;

import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;

public class RecipeSearchState {

    // 现在 ingredients 全部用 String 表示
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private String error;

    public RecipeSearchState() {
    }

    // Copy constructor
    public RecipeSearchState(RecipeSearchState copy) {
        this.ingredients = new ArrayList<>(copy.ingredients);
        this.recipes = new ArrayList<>(copy.recipes);
        this.error = copy.error;
    }

    // ====== Getters ======
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getError() {
        return error;
    }

    // ====== Setters ======
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setError(String error) {
        this.error = error;
    }
}

