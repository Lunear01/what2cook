package view;

import entity.Recipe;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.CookingListState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class CookingListView extends JPanel implements PropertyChangeListener {

    private final CookingListViewModel viewModel;

    private final JLabel titleLabel = new JLabel("My Cooking List");
    private final JList<String> recipeList = new JList<>();
    private final JLabel statusLabel = new JLabel(" ");

    public CookingListView(CookingListViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        final JScrollPane scrollPane = new JScrollPane(recipeList);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof CookingListState) {
            final CookingListState state = (CookingListState) evt.getNewValue();
            updateFromState(state);
        }
    }
}
