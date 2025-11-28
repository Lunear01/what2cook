package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

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
    //增加cookinglist的内容
    private final JButton addToCookingListButton = new JButton("Add to Cooking List");
    private final JButton viewCookingListButton = new JButton("View My Cooking List");

    // 当前选中的菜谱（点击某个 card 时记录）
    private Recipe selectedRecipe = null;
    private RecipeSearchController controller;
    private AddToCookingListController cookingListController;
    private Runnable onOpenCookingList;
    private String currentUsername;

    //结束


    public RecipeSearchView(RecipeSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ingredientsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //增加cookinglist内容
        addToCookingListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewCookingListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //

        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);

        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));
        final int recipesWidth = 500;
        final int recipesHeight = 350;
        recipesScrollPane.setPreferredSize(new Dimension(recipesWidth, recipesHeight));


        // 添加到 cooking list
        addToCookingListButton.addActionListener(e -> {
            if (cookingListController == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cooking list is not configured.",
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
            if (selectedRecipe == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please click a recipe card first.",
                        "No recipe selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            //调用
            cookingListController.add(currentUsername, selectedRecipe);
        });
        //打开
        viewCookingListButton.addActionListener(e -> {
            if (onOpenCookingList != null) {
                onOpenCookingList.run();
            }
        });


        add(titleLabel);
        add(ingredientsTitleLabel);
        add(ingredientsArea);
        add(recipesScrollPane);
        add(resultsCountLabel);

        //加按钮
        add(addToCookingListButton);
        add(viewCookingListButton);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Object newValue = evt.getNewValue();

        //测试
        if (newValue instanceof RecipeSearchState) {
            final RecipeSearchState state = (RecipeSearchState) newValue;

            System.out.println("DEBUG recipes size = " +
                    (state.getRecipes() == null ? "null" : state.getRecipes().size()));

            updateIngredients(state.getIngredients());
            updateRecipes(state.getRecipes());

        }


        if (newValue instanceof RecipeSearchState) {
            final RecipeSearchState state = (RecipeSearchState) newValue;

            updateIngredients(state.getIngredients());
            updateRecipes(state.getRecipes());
            updateResultCount(state.getRecipes());

            if (state.getError() != null) {
                JOptionPane.showMessageDialog(
                        this,
                        state.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void updateIngredients(List<Ingredient> ingredients) {

        final String text;

        if (ingredients == null) {
            text = "";
        }
        else if (ingredients.isEmpty()) {
            text = "";
        }
        else {
            final StringBuilder sb = new StringBuilder();
            for (Ingredient ing : ingredients) {
                sb.append(ing.getName());
                sb.append(", ");
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

        //刷新选中的recipe
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
        final int count;

        if (recipes == null) {
            count = 0;
        }
        else {
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //点击记录选中recipe
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


    //加
    public void setCookingListController(AddToCookingListController cookingListController) {
        this.cookingListController = cookingListController;
    }
    public void setOnOpenCookingList(Runnable onOpenCookingList) {
        this.onOpenCookingList = onOpenCookingList;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
