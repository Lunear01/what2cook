package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import entity.Ingredient;
import entity.Recipe;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchState;
import interface_adapter.recipe_search.RecipeSearchViewModel;

public class RecipeSearchView extends JPanel implements PropertyChangeListener {

    private final RecipeSearchViewModel viewModel;

    private final JLabel titleLabel = new JLabel("What2Cook");
    private final JLabel ingredientsTitleLabel = new JLabel("Current ingredients:");
    private final JTextArea ingredientsArea = new JTextArea(3, 40);

    private final JPanel recipesPanel = new JPanel();
    private final JScrollPane recipesScrollPane = new JScrollPane(recipesPanel);

    private final JLabel resultsCountLabel = new JLabel("0 results");

    private RecipeSearchController controller;

    public RecipeSearchView(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingredientsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);

        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        recipesScrollPane.setPreferredSize(new Dimension(500, 350));

        add(titleLabel);
        add(ingredientsTitleLabel);
        add(ingredientsArea);
        add(recipesScrollPane);
        add(resultsCountLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!(evt.getNewValue() instanceof RecipeSearchState)) return;
        final RecipeSearchState state = (RecipeSearchState) evt.getNewValue();

        updateIngredients(state.getIngredients());
        updateRecipes(state.getRecipes());
        updateResultCount(state.getRecipes());

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateIngredients(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            ingredientsArea.setText("");
            return;
        }

        final StringBuilder sb = new StringBuilder();
        for (Ingredient ing : ingredients) {
            sb.append(ing.getName()).append(", ");
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);

        ingredientsArea.setText(sb.toString());
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
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon icon = new ImageIcon();
        final String path = recipe.getImage();

        if (path != null && !path.isEmpty()) {
            final ImageIcon raw = new ImageIcon(path);
            final Image scaled = raw.getImage().getScaledInstance(440, 140, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaled);
        }

        final JLabel imgLabel = new JLabel(icon, SwingConstants.CENTER);
        final JLabel title = new JLabel(recipe.getTitle(), SwingConstants.CENTER);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(title, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
}
