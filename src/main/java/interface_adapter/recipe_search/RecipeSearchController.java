package interface_adapter.recipe_search;

import java.util.List;

import javax.swing.*;

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

            ImageIcon icon = null;
            if (recipe.getImage() != null) {
                icon = loadImage(recipe.getImage());
            }

            final StringBuilder sb = new StringBuilder();
            sb.append("Title: ").append(recipe.getTitle()).append("\n");
            sb.append("Calories: ").append(recipe.getCalories()).append("\n");
            sb.append("Health Score: ").append(recipe.getHealthScore()).append("\n");

            sb.append("\nIngredients:\n");
            for (Ingredient ing : recipe.getIngredients()) {
                sb.append("  - ").append(ing.getName()).append("\n");
            }

            sb.append("\nInstructions:\n");
            sb.append(recipe.getInstructions());

            JOptionPane.showMessageDialog(
                    null,
                    sb.toString(),
                    recipe.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE,
                    icon
            );
        }
    }
    private ImageIcon loadImage(String urlStr) {
        try {
            java.net.URL url = new java.net.URL(urlStr);
            java.awt.Image img = javax.imageio.ImageIO.read(url);

            // Scaling if needed
            img = img.getScaledInstance(300, -1, java.awt.Image.SCALE_SMOOTH);

            return new ImageIcon(img);
        }
        catch (Exception e) {
            System.out.println("Failed to load image: " + e.getMessage());
            return null;
        }
    }
}

