package view;

import entity.Recipe;
import interface_adapter.favoritelist.AddFavoriteRecipeController;

import javax.swing.*;
import java.awt.*;

/**
 * Shows instructions for one recipe, with a button to add it to favorites.
 */
public class RecipeInstructionView extends JPanel {

    private Recipe currentRecipe;
    private String currentUsername;

    private AddFavoriteRecipeController favoriteController;
    private Runnable onBackToRecipeList;

    private final JLabel titleLabel = new JLabel("Recipe Instructions");
    private final JTextArea instructionsArea = new JTextArea(10, 40);
    private final JButton addToFavoritesButton =
            new JButton("Add to Favorites");

    public RecipeInstructionView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        final JScrollPane scrollPane = new JScrollPane(instructionsArea);
        scrollPane.setPreferredSize(new Dimension(450, 250));

        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(addToFavoritesButton);
        add(Box.createVerticalStrut(10));

        addToFavoritesButton.addActionListener(e -> {
            if (favoriteController == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Favorite feature is not configured.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (currentUsername == null || currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "User is not logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (currentRecipe == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No recipe selected.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                favoriteController.add(currentUsername, currentRecipe);

                JOptionPane.showMessageDialog(
                        this,
                        "Recipe added to your favorites.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (onBackToRecipeList != null) {
                    onBackToRecipeList.run();
                }
            }
            catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add recipe to favorites: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    public void setRecipe(Recipe recipe) {
        this.currentRecipe = recipe;

        if (recipe != null) {
            titleLabel.setText("Instructions for: " + recipe.getTitle());
            instructionsArea.setText("Cooking instructions for this recipe...");
        }
        else {
            titleLabel.setText("Recipe Instructions");
            instructionsArea.setText("");
        }
    }

    public void setFavoriteController(AddFavoriteRecipeController controller) {
        this.favoriteController = controller;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public void setOnBackToRecipeList(Runnable onBackToRecipeList) {
        this.onBackToRecipeList = onBackToRecipeList;
    }
}
