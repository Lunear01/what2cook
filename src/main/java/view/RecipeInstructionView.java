package view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import entity.Recipe;
import interface_adapter.favoritelist.AddFavoriteRecipeController;

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
    private final String errorE = "Error";

    public RecipeInstructionView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        final JScrollPane scrollPane = new JScrollPane(instructionsArea);
        final int scrollPaneWidth = 450;
        final int scrollPaneHeight = 250;
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeight));

        final int verticalStrutHeight = 10;
        add(Box.createVerticalStrut(verticalStrutHeight));
        add(titleLabel);
        add(Box.createVerticalStrut(verticalStrutHeight));
        add(scrollPane);
        add(Box.createVerticalStrut(verticalStrutHeight));
        add(addToFavoritesButton);
        add(Box.createVerticalStrut(verticalStrutHeight));

        addToFavoritesButton.addActionListener(e -> {
            if (favoriteController == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Favorite feature is not configured.",
                        errorE,
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (currentUsername == null || currentUsername.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "User is not logged in.",
                        errorE,
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (currentRecipe == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No recipe selected.",
                        errorE,
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

    /**
     * Updates the instruction view with the given recipe.
     *
     * @param recipe the recipe to display, or null to clear the view
     */
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
