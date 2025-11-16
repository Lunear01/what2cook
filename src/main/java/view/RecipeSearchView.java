package view;

import entity.Recipe;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchState;
import interface_adapter.recipe_search.RecipeSearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Main GUI page for "What2Cook".
 * Top: current ingredients (list of Strings),
 * Center: scrollable list of recipe cards (image + title),
 * Bottom: number of search results.
 */
public class RecipeSearchView extends JPanel implements PropertyChangeListener {

    private final RecipeSearchViewModel viewModel;

    // Title
    private final JLabel titleLabel = new JLabel("What2Cook");

    // Top: ingredients display
    private final JLabel ingredientsTitleLabel = new JLabel("Current ingredients:");
    private final JTextArea ingredientsArea = new JTextArea(3, 40);

    // Center: recipes display
    private final JPanel recipesPanel = new JPanel();
    private final JScrollPane recipesScrollPane = new JScrollPane(recipesPanel);

    // Bottom: results count
    private final JLabel resultsCountLabel = new JLabel("0 results");

    // Controller (set externally)
    private RecipeSearchController controller;

    public RecipeSearchView(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // ===== Layout =====
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingredientsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ingredients box
        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);

        // recipes panel vertical layout
        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        recipesScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        recipesScrollPane.setPreferredSize(new Dimension(500, 350));

        // ===== Add components =====
        this.add(titleLabel);
        this.add(ingredientsTitleLabel);
        this.add(ingredientsArea);
        this.add(recipesScrollPane);
        this.add(resultsCountLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RecipeSearchState state = (RecipeSearchState) evt.getNewValue();
        updateIngredients(state.getIngredients());
        updateRecipes(state.getRecipes());
        updateResultCount(state.getRecipes());

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ------------------- Update UI ------------------- */

    private void updateIngredients(List<String> ingredients) {
        if (ingredients != null && !ingredients.isEmpty()) {
            ingredientsArea.setText(String.join(", ", ingredients));
        } else {
            ingredientsArea.setText("");
        }
    }

    private void updateRecipes(List<Recipe> recipes) {
        recipesPanel.removeAll();

        if (recipes != null) {
            for (Recipe recipe : recipes) {
                recipesPanel.add(createRecipeCard(recipe));
            }
        }

        recipesPanel.revalidate();
        recipesPanel.repaint();
    }

    private void updateResultCount(List<Recipe> recipes) {
        int count = (recipes == null) ? 0 : recipes.size();
        resultsCountLabel.setText(count + " results");
    }