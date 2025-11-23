package view;

import entity.Recipe;
import interface_adapter.cookinglist.CookingListState;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.RemoveFromCookingListController;
import interface_adapter.recipe_search.RecipeSearchController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class CookingListView extends JPanel implements PropertyChangeListener {

    private final CookingListViewModel viewModel;
    private final RecipeSearchController recipeSearchController;

    private final RemoveFromCookingListController removeController;
    private final String currentUsername;

    private final DefaultListModel<Recipe> listModel = new DefaultListModel<>();
    private final JList<Recipe> list = new JList<>(listModel);
    private final JLabel statusLabel = new JLabel("");

    public CookingListView(CookingListViewModel viewModel,
                           RecipeSearchController recipeSearchController,
                           RemoveFromCookingListController removeController,
                           String currentUsername) {

        this.viewModel = viewModel;
        this.recipeSearchController = recipeSearchController;
        this.removeController = removeController;
        this.currentUsername = currentUsername;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        add(new JLabel("Your Cooking List"), BorderLayout.NORTH);

        // 用 title 渲染
        list.setCellRenderer((jList, recipe, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(recipe.getTitle());
            label.setOpaque(true);
            if (isSelected) label.setBackground(jList.getSelectionBackground());
            return label;
        });

        // 点击弹详情（复用 RecipeSearchController.openRecipe）
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Recipe selected = list.getSelectedValue();
                if (selected != null) {
                    recipeSearchController.openRecipe(selected);
                }
            }
        });

        add(new JScrollPane(list), BorderLayout.CENTER);

        // Bottom：Remove 按钮 + status
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JButton removeBtn = new JButton("Remove Selected");
        removeBtn.addActionListener(e -> {
            Recipe selected = list.getSelectedValue();
            if (selected != null && removeController != null) {
                removeController.remove(currentUsername, selected);
            }
        });

        bottomPanel.add(removeBtn, BorderLayout.WEST);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        render();
    }

    private void render() {
        CookingListState state = viewModel.getState();
        List<Recipe> recipes = state.getPersonalCookingList();

        listModel.clear();
        if (recipes != null) {
            for (Recipe r : recipes) listModel.addElement(r);
        }

        String msg = state.getStatusMessage();
        statusLabel.setText(msg == null ? "" : msg);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        render();
    }
}
