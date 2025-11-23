package view;

import entity.Recipe;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchState;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import interface_adapter.cookinglist.AddToCookingListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Main GUI page for "What2Cook".
 * Top: search bar + current ingredients,
 * Center: scrollable list of recipe cards,
 * Bottom: number of search results.
 */
public class RecipeSearchView extends JPanel implements PropertyChangeListener {

    private final RecipeSearchViewModel viewModel;

    // Title
    private final JLabel titleLabel = new JLabel("What2Cook");

    private final JTextField searchField = new JTextField(24);
    private final JButton searchButton = new JButton("Search");

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

    // Add to cooking list controller + current user
    private AddToCookingListController addToCookingListController;
    private String currentUsername;

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

        // ✅ 搜索面板
        JPanel searchPanel = new JPanel();
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(new JLabel("Ingredients (comma separated): "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            if (controller != null) {
                controller.searchByIngredients(searchField.getText().trim());
            }
        });

        // ===== Add components =====
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(8));
        this.add(searchPanel);
        this.add(Box.createVerticalStrut(8));
        this.add(ingredientsTitleLabel);
        this.add(ingredientsArea);
        this.add(recipesScrollPane);
        this.add(resultsCountLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final RecipeSearchState state = (RecipeSearchState) event.getNewValue();
        updateIngredients(state.getIngredients());
        updateRecipes(state.getRecipes());
        updateResultCount(state.getRecipes());

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateIngredients(List<String> ingredients) {
        if (ingredients != null && !ingredients.isEmpty()) {
            ingredientsArea.setText(String.join(", ", ingredients));
        }
        else {
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
        final int count = (recipes == null) ? 0 : recipes.size();
        resultsCountLabel.setText(count + " results");
    }

    private JPanel createRecipeCard(Recipe recipe) {
        final JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(460, 200));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // image
        final ImageIcon icon;
        final String path = recipe.getImage();

        if (path != null && !path.isEmpty()) {
            final ImageIcon raw = new ImageIcon(path);
            final Image scaled = raw.getImage().getScaledInstance(440, 140, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }
        else {
            icon = new ImageIcon();
        }

        final JLabel imgLabel = new JLabel(icon);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // title
        JLabel tLabel = new JLabel(recipe.getTitle());
        tLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add button
        JButton addBtn = new JButton("Add to Cooking List");
        addBtn.addActionListener(e -> {
            if (addToCookingListController != null && currentUsername != null) {
                addToCookingListController.add(currentUsername, recipe);
            }
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(tLabel, BorderLayout.CENTER);
        bottom.add(addBtn, BorderLayout.EAST);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    controller.openRecipe(recipe);
                }
            }
        });

        return card;
    }

    public void setController(RecipeSearchController controller) {
        this.controller = controller;
    }

    public void setAddToCookingListController(AddToCookingListController controller,
                                              String username) {
        this.addToCookingListController = controller;
        this.currentUsername = username;
    }
}

