package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import entity.Ingredient;
import entity.Recipe;
import interface_adapter.cookinglist.AddToCookingListController;
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

    // Buttons
    private final JButton backButton = new JButton("Back");
    private final JButton addToCookingListButton = new JButton("Add to Cooking List");
    private final JButton viewCookingListButton = new JButton("View My Cooking List");
    private final JButton viewFavoritesButton = new JButton("View My Favorites");

    private Recipe selectedRecipe;

    private RecipeSearchController controller;
    private AddToCookingListController cookingListController;

    private Runnable onOpenCookingList;
    private Runnable onOpenFavorites;
    private Runnable onBack;
    private Consumer<Recipe> onOpenInstruction;

    private String currentUsername;
    private final String errorE = "Error";

    public RecipeSearchView(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        configureAlignment();
        configureTextAreasAndPanels();
        configureListeners();
        addComponentsToLayout();
    }

    private void configureAlignment() {
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingredientsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addToCookingListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewCookingListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewFavoritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void configureTextAreasAndPanels() {
        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);

        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        final int recipesWidth = 500;
        final int recipesHeight = 350;
        recipesScrollPane.setPreferredSize(new Dimension(recipesWidth, recipesHeight));
    }

    private void configureListeners() {
        backButton.addActionListener(event -> handleBack());

        addToCookingListButton.addActionListener(event -> handleAddToCookingList());

        viewCookingListButton.addActionListener(event -> {
            if (onOpenCookingList != null) {
                onOpenCookingList.run();
            }
        });

        viewFavoritesButton.addActionListener(event -> {
            if (onOpenFavorites != null) {
                onOpenFavorites.run();
            }
        });
    }

    private void addComponentsToLayout() {
        add(backButton);
        add(titleLabel);
        add(ingredientsTitleLabel);
        add(ingredientsArea);
        add(recipesScrollPane);
        add(resultsCountLabel);

        add(addToCookingListButton);
        add(viewCookingListButton);
        add(viewFavoritesButton);
    }

    private void handleBack() {
        if (onBack != null) {
            onBack.run();
        }
    }

    private void handleAddToCookingList() {
        System.out.println("\n========== ADD TO COOKING LIST CLICKED ==========");

        boolean canProceed = true;

        if (cookingListController == null) {
            System.out.println("ERROR: cookingListController is null");
            JOptionPane.showMessageDialog(
                    this,
                    "Cooking list is not configured.",
                    errorE,
                    JOptionPane.ERROR_MESSAGE
            );
            canProceed = false;
        }
        else if (currentUsername == null || currentUsername.isEmpty()) {
            System.out.println("ERROR: currentUsername is null or empty");
            JOptionPane.showMessageDialog(
                    this,
                    "User is not logged in.",
                    errorE,
                    JOptionPane.ERROR_MESSAGE
            );
            canProceed = false;
        }
        else if (selectedRecipe == null) {
            System.out.println("ERROR: selectedRecipe is null");
            JOptionPane.showMessageDialog(
                    this,
                    "Please click a recipe card first.",
                    "No recipe selected",
                    JOptionPane.WARNING_MESSAGE
            );
            canProceed = false;
        }

        if (canProceed) {
            System.out.println("DEBUG RecipeSearchView: All checks passed");
            System.out.println("  - Username: " + currentUsername);
            System.out.println("  - Recipe ID: " + selectedRecipe.getId());
            System.out.println("  - Recipe Title: " + selectedRecipe.getTitle());
            System.out.println("  - Recipe Calories: " + selectedRecipe.getCalories());
            System.out.println("  - Recipe Health Score: " + selectedRecipe.getHealthScore());
            System.out.println("  - Recipe Ingredients count: " +
                (selectedRecipe.getIngredients() != null ? selectedRecipe.getIngredients().size() : "null"));

            System.out.println("DEBUG RecipeSearchView: Calling controller.add()...");
            cookingListController.add(currentUsername, selectedRecipe);
            System.out.println("DEBUG RecipeSearchView: controller.add() completed");

            // Show success message to user
            JOptionPane.showMessageDialog(
                    this,
                    selectedRecipe.getTitle() + " has been processed!\nCheck 'View My Cooking List' to see the result.",
                    "Add to Cooking List",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        System.out.println("========== ADD TO COOKING LIST END ==========\n");
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Object newValue = evt.getNewValue();

        if (newValue instanceof RecipeSearchState) {
            final RecipeSearchState state = (RecipeSearchState) newValue;

            // --- Fix AvoidInlineConditionals ---
            final List<Recipe> recipes = state.getRecipes();
            final String sizeText;
            if (recipes == null) {
                sizeText = "null";
            }
            else {
                sizeText = String.valueOf(recipes.size());
            }

            System.out.println("DEBUG recipes size = " + sizeText);

            updateIngredients(state.getIngredients());
            updateRecipes(recipes);
            updateResultCount(recipes);

            if (state.getError() != null) {
                JOptionPane.showMessageDialog(
                        this,
                        state.getError(),
                        errorE,
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void updateIngredients(List<Ingredient> ingredients) {
        final String text;

        if (ingredients == null || ingredients.isEmpty()) {
            text = "";
        }
        else {
            final StringBuilder sb = new StringBuilder();
            for (Ingredient ing : ingredients) {
                sb.append(ing.getName()).append(", ");
            }
            if (sb.length() > 2) {
                sb.setLength(sb.length() - 2);
            }
            text = sb.toString();
        }

        ingredientsArea.setText(text);
    }

    private void updateRecipes(List<Recipe> recipes) {
        recipesPanel.removeAll();

        selectedRecipe = null;

        if (recipes != null) {
            for (Recipe recipe : recipes) {
                recipesPanel.add(createRecipeCard(recipe));
            }
        }

        recipesPanel.revalidate();
        recipesPanel.repaint();
    }

    private void updateResultCount(List<Recipe> recipes) {
        int count = 0;
        if (recipes != null) {
            count = recipes.size();
        }
        resultsCountLabel.setText(count + " results");
    }

    private JPanel createRecipeCard(Recipe recipe) {
        System.out.println("DEBUG create card for: " + recipe.getTitle());
        final JPanel card = new JPanel(new BorderLayout());
        final int cardWidth = 460;
        final int cardHeight = 200;
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, cardHeight));
        final int cardBorder = 10;
        card.setBorder(BorderFactory.createEmptyBorder(cardBorder, cardBorder, cardBorder, cardBorder));

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
            public void mouseClicked(java.awt.event.MouseEvent event) {
                selectedRecipe = recipe;

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

    public void setCookingListController(AddToCookingListController cookingListController) {
        this.cookingListController = cookingListController;
    }

    public void setOnOpenCookingList(Runnable onOpenCookingList) {
        this.onOpenCookingList = onOpenCookingList;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public void setOnOpenFavorites(Runnable onOpenFavorites) {
        this.onOpenFavorites = onOpenFavorites;
    }

    public void setOnOpenInstruction(Consumer<Recipe> onOpenInstruction) {
        this.onOpenInstruction = onOpenInstruction;
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
