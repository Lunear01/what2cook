package view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interface_adapter.ingredient_search.IngredientSearchState;
import interface_adapter.ingredient_search.IngredientSearchViewModel;

/**
 * Ingredient Search View.
 */
public class IngredientSearchView extends JPanel
        implements ActionListener, PropertyChangeListener {

    public static final String VIEW_NAME = "ingredient search";

    private final IngredientSearchViewModel viewModel;

    private final JLabel titleLabel = new JLabel("Enter Your Ingredients");
    private final JLabel inputLabel = new JLabel("Ingredient:");
    private final JTextField inputField = new JTextField(20);
    private final JButton addButton = new JButton("Add");
    private final JButton searchButton = new JButton("Search Recipes");
    private final JButton openFridgeButton = new JButton("View My Fridge");

    private final JTextArea ingredientArea = new JTextArea(10, 40);
    private final JScrollPane ingredientScrollPane = new JScrollPane(ingredientArea);

    private Runnable onNext;

    private Consumer<String> onAddToFridge;

    private Runnable onOpenFridge;

    public IngredientSearchView(IngredientSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final int border = 20;
        setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final float titleSize = 18f;
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, titleSize));

        final JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(addButton);

        ingredientArea.setEditable(false);
        ingredientArea.setLineWrap(true);
        ingredientArea.setWrapStyleWord(true);

        ingredientScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openFridgeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        final int height10 = 10;
        final int height15 = 15;

        add(titleLabel);
        add(Box.createVerticalStrut(height15));
        add(inputPanel);
        add(Box.createVerticalStrut(height10));
        add(ingredientScrollPane);
        add(Box.createVerticalStrut(height15));
        add(searchButton);
        add(Box.createVerticalStrut(height10));
        add(openFridgeButton);

        addButton.addActionListener(this);
        searchButton.addActionListener(this);
        openFridgeButton.addActionListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newVal = evt.getNewValue();
        if (newVal instanceof IngredientSearchState) {
            final IngredientSearchState state = (IngredientSearchState) newVal;
            updateIngredientArea(state.getIngredients());
            if (state.getError() != null && !state.getError().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, state.getError(), "Error", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void updateIngredientArea(List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            ingredientArea.setText("");
            return;
        }

        final StringBuilder sb = new StringBuilder();
        for (String ing : ingredients) {
            sb.append(ing).append("\n");
        }
        ingredientArea.setText(sb.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object src = e.getSource();

        if (src == addButton) {
            final String name = inputField.getText().trim();
            if (name.isEmpty()) {
                return;
            }

            final IngredientSearchState oldState = viewModel.getState();
            final IngredientSearchState newState = new IngredientSearchState(oldState);

            final List<String> list = new ArrayList<>(newState.getIngredients());
            list.add(name);
            newState.setIngredients(list);
            newState.setCurrentInput("");

            viewModel.setState(newState);
            viewModel.firePropertyChanged();

            inputField.setText("");

            if (onAddToFridge != null) {
                onAddToFridge.accept(name);
            }
        }
        else if (src == searchButton) {
            if (onNext != null) {
                onNext.run();
            }
        }
        else if (src == openFridgeButton) {
            if (onOpenFridge != null) {
                onOpenFridge.run();
            }
        }
    }

    public void setOnNext(Runnable onNext) {
        this.onNext = onNext;
    }

    public void setOnAddToFridge(Consumer<String> onAddToFridge) {
        this.onAddToFridge = onAddToFridge;
    }

    public void setOnOpenFridge(Runnable onOpenFridge) {
        this.onOpenFridge = onOpenFridge;
    }
}
