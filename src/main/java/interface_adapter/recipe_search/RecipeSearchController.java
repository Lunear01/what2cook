package interface_adapter.recipe_search;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import entity.Ingredient;
import entity.Recipe;
import interface_adapter.favoritelist.AddFavoriteRecipeController;
import use_case.recipe_search.RecipeSearchInputBoundary;
import use_case.recipe_search.RecipeSearchInputData;

public class RecipeSearchController {

    private final RecipeSearchInputBoundary interactor;

    private AddFavoriteRecipeController favoriteController;

    private String currentUsername;

    private final String errorE = "Error";

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
     * Injects the controller used to add recipes to favorites.
     *
     * @param favoriteController the controller of favorite.
     *
     * */
    public void setFavoriteController(AddFavoriteRecipeController favoriteController) {
        this.favoriteController = favoriteController;
    }

    /**
     * Sets the username of the currently logged-in user.
     *
     * @param username the name of user.
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

            if (choice != 0) {
                keepOpen = false;
                break;
            }

            // choice == 0 → Add to Favorite List
            if (favoriteController == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Favorite feature is not configured.",
                        errorE,
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
            if (currentUsername == null || currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "User is not logged in.",
                        errorE,
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            final String message =
                    favoriteController.addAndGetMessage(currentUsername, recipe);

            JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Favorite",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Loads an image from the given URL string and scales it to a fixed width.
     *
     * @param urlStr the URL of the image to load
     * @return an ImageIcon if loading succeeds; otherwise null
     */
    private ImageIcon loadImage(String urlStr) {
        ImageIcon result = null;

        try {
            final java.net.URL url = new java.net.URL(urlStr);
            java.awt.Image img = javax.imageio.ImageIO.read(url);

            if (img != null) {
                final int width = 300;
                img = img.getScaledInstance(width, -1, java.awt.Image.SCALE_SMOOTH);
                result = new ImageIcon(img);
            }
        }
        catch (Exception ex) {
            System.out.println("Failed to load image: " + ex.getMessage());
            // result remains null
        }

        return result;
    }

}

