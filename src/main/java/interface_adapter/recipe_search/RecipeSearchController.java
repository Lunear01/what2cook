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
        for (Ingredient ing : recipe.getIngredientNames()) {
            sb.append("  - ").append(ing.getName()).append("\n");
        }

        sb.append("\nInstructions:\n");
        sb.append(recipe.getInstructions());

        // 两个按钮：Add to Favorite List / Close
        final Object[] options = {"Add to Favorite List", "Close"};

        final int choice = JOptionPane.showOptionDialog(
                null,
                sb.toString(),
                recipe.getTitle(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                icon,
                options,
                options[1]
        );

        // 点击了 "Add to Favorite List"
        if (choice == 0) {
            if (favoriteController == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Favorite feature is not configured.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (currentUsername == null || currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "User is not logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // 调用 use case，把这道菜加入 favorite list
            favoriteController.add(currentUsername, recipe);

            // 弹一个 OK 的提示框
            JOptionPane.showMessageDialog(
                    null,
                    "Recipe added to your favorite list!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // 之后弹窗就已经关了，用户自然回到 recipe 列表界面
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

