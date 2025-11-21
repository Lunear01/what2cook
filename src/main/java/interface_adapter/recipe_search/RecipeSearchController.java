package interface_adapter.recipe_search;

import javax.swing.JOptionPane;

import entity.Ingredient;
import entity.Recipe;

public class RecipeSearchController {
    public void openRecipe(Recipe recipe) {
        if (recipe == null) return;

        final StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(recipe.getTitle()).append("\n");
        sb.append("Calories: ").append(recipe.getCalories()).append("\n");
        sb.append("Health Score: ").append(recipe.getHealthScore()).append("\n");
        sb.append("Ingredients:\n");
        for (Ingredient ing : recipe.getIngredientNames()) {
            sb.append("  - ").append(ing).append("\n");
        }
        sb.append("\nInstructions:\n").append(recipe.getInstructions());

        JOptionPane.showMessageDialog(null, sb.toString(),
                recipe.getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

}
