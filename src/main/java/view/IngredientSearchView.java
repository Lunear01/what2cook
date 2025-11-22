package view;

import interface_adapter.ingredient_search.IngredientSearchState;
import interface_adapter.ingredient_search.IngredientSearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class IngredientSearchView extends JPanel
        implements ActionListener, PropertyChangeListener {

    private final IngredientSearchViewModel viewModel;

    private final JLabel titleLabel = new JLabel("Enter Your Ingredients");
    private final JTextField inputField = new JTextField(20);
    private final JButton addButton = new JButton("Add");
    private final JButton nextButton = new JButton("Search Recipes");

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> ingredientList = new JList<>(listModel);

    private final JLabel errorLabel = new JLabel(" ");

    private Runnable onNext;

    public IngredientSearchView(IngredientSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Ingredient:"));
        inputPanel.add(inputField);
        inputPanel.add(addButton);

        JScrollPane scrollPane = new JScrollPane(ingredientList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titleLabel);
        add(Box.createVerticalStrut(10));
        add(inputPanel);
        add(Box.createVerticalStrut(10));
        add(scrollPane);
        add(Box.createVerticalStrut(10));
        add(nextButton);
        add(Box.createVerticalStrut(10));
        add(errorLabel);

        addButton.addActionListener(this);
        nextButton.addActionListener(this);
    }

    public void setOnNext(Runnable onNext) {
        this.onNext = onNext;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == addButton) {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                IngredientSearchState oldState = viewModel.getState();
                IngredientSearchState newState = new IngredientSearchState(oldState);
                newState.getIngredients().add(text);
                newState.setCurrentInput("");
                newState.setError(null);
                viewModel.setState(newState);
                viewModel.firePropertyChanged();

                inputField.setText("");
            }
        } else if (src == nextButton) {
            if (onNext != null) {
                onNext.run();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newVal = evt.getNewValue();
        if (!(newVal instanceof IngredientSearchState)) return;

        IngredientSearchState state = (IngredientSearchState) newVal;

        listModel.clear();
        for (String ing : state.getIngredients()) {
            listModel.addElement(ing);
        }

        String err = state.getError();
        if (err != null && !err.isEmpty()) {
            errorLabel.setText(err);
        } else {
            errorLabel.setText(" ");
        }
    }
}