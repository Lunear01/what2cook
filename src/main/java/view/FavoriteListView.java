package view;

import entity.Recipe;
import interface_adapter.favoritelist.FavoriteListState;
import interface_adapter.favoritelist.FavoriteListViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * A panel that uses vertical BoxLayouts to display favorite recipes in a scrollable list,
 * along with a title and status label, updating automatically when the FavoriteListState changes.
 */
public class FavoriteListView extends JPanel implements PropertyChangeListener {

    private final FavoriteListViewModel viewModel;

    private final JLabel titleLabel = new JLabel("My Favorite Recipes");
    private final JPanel listPanel = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane(listPanel);
    private final JLabel statusLabel = new JLabel("");

    public FavoriteListView(FavoriteListViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        scrollPane.setPreferredSize(new Dimension(450, 300));

        add(Box.createVerticalStrut(10));
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(statusLabel);
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
        listPanel.removeAll();

        if (favorites != null && !favorites.isEmpty()) {
            for (Recipe recipe : favorites) {
                final JLabel label = new JLabel("• " + recipe.getTitle());
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                listPanel.add(label);
            }
        }
        else {
            final JLabel empty = new JLabel("No favorite recipes yet.");
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listPanel.add(empty);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
