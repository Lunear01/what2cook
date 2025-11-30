package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import entity.Recipe;
import interface_adapter.cookinglist.CookingListState;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.SortCookingListController;
import use_case.cookinglist.SortCookingListInputData.SortType;

public class CookingListView extends JPanel implements PropertyChangeListener {

    private final CookingListViewModel viewModel;
    private final SortCookingListController sortController;
    private String currentUsername;

    private final JLabel titleLabel = new JLabel("My Cooking List");
    private final JList<String> recipeList = new JList<>();
    private final JLabel statusLabel = new JLabel(" ");

    private final JButton backButton = new JButton("Back");
    private final JButton sortByHealthButton = new JButton("Sort by Health Score");
    private final JButton sortByCaloriesButton = new JButton("Sort by Calories");
    private Runnable onBack;
    private Consumer<Recipe> onOpenRecipe;

    public CookingListView(CookingListViewModel viewModel, SortCookingListController sortController) {
        this.viewModel = viewModel;
        this.sortController = sortController;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JScrollPane scrollPane = new JScrollPane(recipeList);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        final JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(backButton);
        buttonRow.add(sortByHealthButton);
        buttonRow.add(sortByCaloriesButton);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        bottomPanel.add(buttonRow);
        bottomPanel.add(statusLabel);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        recipeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                handleRecipeDoubleClick(event);
            }
        });

        backButton.addActionListener(event -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        sortByHealthButton.addActionListener(event -> {
            handleSortByHealth();
        });

        sortByCaloriesButton.addActionListener(event -> {
            handleSortByCalories();
        });

        updateFromState(viewModel.getState());
    }

    private void handleRecipeDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2 && !recipeList.isSelectionEmpty()) {
            final int index = recipeList.getSelectedIndex();
            final List<Recipe> list = viewModel.getPersonalCookingList();
            if (index >= 0 && index < list.size() && onOpenRecipe != null) {
                final Recipe selected = list.get(index);
                onOpenRecipe.accept(selected);
            }
        }
    }

    private void handleSortByHealth() {
        if (currentUsername != null) {
            try {
                sortController.sort(currentUsername, SortType.BY_HEALTH_SCORE);
            }
            catch (Exception ex) {
                showErrorDialog("Failed to sort: " + ex.getMessage());
            }
        }
    }

    private void handleSortByCalories() {
        if (currentUsername != null) {
            try {
                sortController.sort(currentUsername, SortType.BY_CALORIES);
            }
            catch (Exception ex) {
                showErrorDialog("Failed to sort: " + ex.getMessage());
            }
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void updateFromState(CookingListState state) {
        final List<Recipe> recipes = state.getPersonalCookingList();
        final String[] titles = recipes.stream()
                .map(Recipe::getTitle)
                .toArray(String[]::new);

        recipeList.setListData(titles);

        final String msg = state.getStatusMessage();
        statusLabel.setText(msg == null ? " " : msg);
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setOnOpenRecipe(Consumer<Recipe> onOpenRecipe) {
        this.onOpenRecipe = onOpenRecipe;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof CookingListState) {
            final CookingListState state = (CookingListState) evt.getNewValue();
            updateFromState(state);
        }
    }
}
