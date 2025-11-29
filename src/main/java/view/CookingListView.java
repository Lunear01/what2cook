package view;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import entity.Recipe;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.CookingListState;

import javax.swing.*;
import java.awt.*;


public class CookingListView extends JPanel implements PropertyChangeListener {

    private final CookingListViewModel viewModel;

    private final JLabel titleLabel = new JLabel("My Cooking List");
    private final JList<String> recipeList = new JList<>();
    private final JLabel statusLabel = new JLabel(" ");

    private final JButton backButton = new JButton("Back");
    private final JButton sortByHealthButton = new JButton("Sort by Health Score");
    private final JButton sortByCaloriesButton = new JButton("Sort by Calories");
    private Runnable onBack;

    public CookingListView(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JScrollPane scrollPane = new JScrollPane(recipeList);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel buttonRow = new JPanel();
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

        backButton.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        sortByHealthButton.addActionListener(e -> {
            List<Recipe> list = new ArrayList<>(viewModel.getPersonalCookingList());
            list.sort(Comparator.comparingInt(Recipe::getHealthScore).reversed());
            viewModel.setPersonalCookingList(list);
        });

        sortByCaloriesButton.addActionListener(e -> {
            List<Recipe> list = new ArrayList<>(viewModel.getPersonalCookingList());
            list.sort(Comparator.comparingDouble(Recipe::getCalories));
            viewModel.setPersonalCookingList(list);
        });

        updateFromState(viewModel.getState());
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


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof CookingListState) {
            final CookingListState state = (CookingListState) evt.getNewValue();
            updateFromState(state);
        }
    }
}
