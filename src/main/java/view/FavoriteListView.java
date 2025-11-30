package view;

import entity.Recipe;
import interface_adapter.favoritelist.AddFavoriteRecipeController;
import interface_adapter.favoritelist.FavoriteListState;
import interface_adapter.favoritelist.FavoriteListViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

public class FavoriteListView extends JPanel implements PropertyChangeListener {

    private final FavoriteListViewModel viewModel;

    private AddFavoriteRecipeController favoriteController;
    private String currentUsername;
    private Runnable onBackToRecipes;

    private final JLabel titleLabel = new JLabel("My Favorite Recipes");
    private final JLabel statusLabel = new JLabel("");

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> favoritesList = new JList<>(listModel);

    private final JScrollPane scrollPane = new JScrollPane(favoritesList);

    private final JButton deleteButton = new JButton("Delete Selected");
    private final JButton backButton = new JButton("Back to Recipes");

    // 用来根据选中的 index 找到对应的 Recipe
    private List<Recipe> currentFavorites = Collections.emptyList();

    public FavoriteListView(FavoriteListViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(deleteButton);
        add(Box.createVerticalStrut(5));
        add(backButton);
        add(Box.createVerticalStrut(10));
        add(statusLabel);

        // 点击 Delete
        deleteButton.addActionListener(e -> {
            final int index = favoritesList.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a recipe to delete.",
                        "No selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
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

            final Recipe recipe = currentFavorites.get(index);
            favoriteController.remove(currentUsername, recipe);
        });

        // 点击 Back
        backButton.addActionListener(e -> {
            if (onBackToRecipes != null) {
                onBackToRecipes.run();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newValue = evt.getNewValue();

        if (newValue instanceof FavoriteListState) {
            final FavoriteListState state = (FavoriteListState) newValue;
            updateList(state.getFavoriteList());
            statusLabel.setText(state.getStatusMessage());
        }
    }

    private void updateList(List<Recipe> favorites) {
        currentFavorites = (favorites == null) ? Collections.emptyList() : favorites;

        listModel.clear();
        if (currentFavorites.isEmpty()) {
            listModel.addElement("No favorite recipes yet.");
            favoritesList.setEnabled(false);
            deleteButton.setEnabled(false);
        }
        else {
            for (Recipe recipe : currentFavorites) {
                listModel.addElement("• " + recipe.getTitle());
            }
            favoritesList.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }

    // ==== setters 让 AppBuilder 注入 ====

    public void setFavoriteController(AddFavoriteRecipeController controller) {
        this.favoriteController = controller;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public void setOnBackToRecipes(Runnable onBackToRecipes) {
        this.onBackToRecipes = onBackToRecipes;
    }
}
