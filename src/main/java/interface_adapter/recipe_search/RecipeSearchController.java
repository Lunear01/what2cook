package interface_adapter.recipe_search;

import java.util.List;

import javax.swing.*;

import interface_adapter.favoritelist.AddFavoriteRecipeController;

import entity.Ingredient;
import entity.Recipe;
import use_case.recipe_search.RecipeSearchInputBoundary;
import use_case.recipe_search.RecipeSearchInputData;

public class RecipeSearchController {

    private final RecipeSearchInputBoundary interactor;

    private AddFavoriteRecipeController favoriteController;

    private String currentUsername;

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

    /** Injects the controller used to add recipes to favorites. */
    public void setFavoriteController(AddFavoriteRecipeController favoriteController) {
        this.favoriteController = favoriteController;
    }

    /**
     * Sets the username of the currently logged-in user.
     */
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    /**
     * Opens a dialog showing detailed recipe information,
     * with a button to add the recipe to the favorite list.
     *
     * @param recipe the selected recipe.
     */
    public void openRecipe(Recipe recipe) {
        if (recipe == null) {
            return;
        }

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

        final Object[] options = {"Add to Favorite List", "Close"};

        boolean keepOpen = true;
        while (keepOpen) {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    sb.toString(),
                    recipe.getTitle(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    icon,
                    options,
                    options[1]
            );

            if (choice != 0) { // choice == 1 或 JOptionPane.CLOSED_OPTION(-1)
                keepOpen = false;
                break;
            }

            // choice == 0 → Add to Favorite List
            if (favoriteController == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Favorite feature is not configured.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                // 继续停在页面上
                continue;
            }
            if (currentUsername == null || currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "User is not logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            try {
                favoriteController.add(currentUsername, recipe);

                JOptionPane.showMessageDialog(
                        null,
                        "Recipe added to your favorite list!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to add recipe to favorites: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
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

