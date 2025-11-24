package use_case.recipe_search;

import java.util.List;
import entity.Ingredient;
import entity.Recipe;

public class RecipeSearchOutputData {

    private final List<Ingredient> ingredients;
    private final List<Recipe> recipes;
    private final String errorMessage;

    public RecipeSearchOutputData(List<Ingredient> ingredients,
                                  List<Recipe> recipes,
                                  String errorMessage) {
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.errorMessage = errorMessage;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
