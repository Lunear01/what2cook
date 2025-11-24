package interface_adapter.recipe_search;

import java.util.List;

import javax.swing.JOptionPane;

import entity.Ingredient;
import entity.Recipe;
import use_case.recipe_search.RecipeSearchInputBoundary;
import use_case.recipe_search.RecipeSearchInputData;

public class RecipeSearchController {

    private final RecipeSearchInputBoundary interactor;

    public RecipeSearchController(RecipeSearchInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called by the view when user clicks "Search".
     *
     * @param ingredients the input ingredients.
     */
    public void searchByIngredients(List<Ingredient> ingredients) {
        final RecipeSearchInputData inputData =
                new RecipeSearchInputData(ingredients);
        interactor.execute(inputData);
    }

    /**
     * Opens a dialog showing detailed recipe information.
     *
     * @param recipe the selected recipe.
     */
    public void openRecipe(Recipe recipe) {
        if (recipe != null) {

            final StringBuilder sb = new StringBuilder();
            sb.append("Title: ").append(recipe.getTitle()).append("\n");
            sb.append("Calories: ").append(recipe.getCalories()).append("\n");
            sb.append("Health Score: ").append(recipe.getHealthScore()).append("\n");

            sb.append("\nIngredients:\n");
            for (Ingredient ing : recipe.getIngredientNames()) {
                sb.append("  - ").append(ing.getName()).append("\n");
            }

            sb.append("\nInstructions:\n");
            sb.append(recipe.getInstructions());

            JOptionPane.showMessageDialog(
                    null,
                    sb.toString(),
                    recipe.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}

