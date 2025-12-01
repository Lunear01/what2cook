package view;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import entity.Recipe;
import interface_adapter.favoritelist.FavoriteListState;
import interface_adapter.favoritelist.FavoriteListViewModel;

public class FavoriteListView extends JPanel implements PropertyChangeListener {

    private final FavoriteListViewModel viewModel;

    private Runnable onBackToRecipes;

    private final JLabel titleLabel = new JLabel("My Favorite Recipes");
    private final JLabel statusLabel = new JLabel("");

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> favoritesList = new JList<>(listModel);

    private final JScrollPane scrollPane = new JScrollPane(favoritesList);

    private final JButton backButton = new JButton("Back to Recipes");

    private List<Recipe> currentFavorites = Collections.emptyList();

    private Consumer<Recipe> onOpenInstruction;

    public FavoriteListView(FavoriteListViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        favoritesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        final int scrollPaneWidth = 450;
        final int scrollPaneHeight = 300;
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeight));

        final int doubleClickCount = 2;
        favoritesList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == doubleClickCount) {
                    final int index = favoritesList.locationToIndex(e.getPoint());
                    if (index >= 0 && index < currentFavorites.size()
                            && onOpenInstruction != null) {
                        final Recipe recipe = currentFavorites.get(index);
                        onOpenInstruction.accept(recipe);
                    }
                }
            }
        });

        backButton.addActionListener(eev -> {
            if (onBackToRecipes != null) {
                onBackToRecipes.run();
            }
        });

        final int verticalStrutHeight = 10;
        final int horizontalStrut5 = 5;
        add(Box.createVerticalStrut(verticalStrutHeight));
        add(titleLabel);
        add(Box.createVerticalStrut(verticalStrutHeight));
        add(scrollPane);
        add(Box.createVerticalStrut(verticalStrutHeight));
        add(backButton);
        add(Box.createVerticalStrut(horizontalStrut5));
        add(statusLabel);

        backButton.addActionListener(eev -> {
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
        if (favorites == null) {
            currentFavorites = Collections.emptyList();
        }
        else {
            currentFavorites = favorites;
        }

        listModel.clear();
        if (currentFavorites.isEmpty()) {
            listModel.addElement("No favorite recipes yet.");
            favoritesList.setEnabled(false);
        }
        else {
            for (Recipe recipe : currentFavorites) {
                listModel.addElement("\u2022" + recipe.getTitle());
            }
            favoritesList.setEnabled(true);
        }
    }

    public void setOnBackToRecipes(Runnable onBackToRecipes) {
        this.onBackToRecipes = onBackToRecipes;
    }

    public void setOnOpenInstruction(Consumer<Recipe> onOpenInstruction) {
        this.onOpenInstruction = onOpenInstruction;
    }
}
