package view;

import entity.Ingredient;
import interface_adapter.fridgemodify.FridgeController;
import interface_adapter.fridgemodify.FridgeState;
import interface_adapter.fridgemodify.FridgeViewModel;

import javax.swing.*;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class FridgeView extends JPanel implements PropertyChangeListener {

    private final FridgeViewModel viewModel;
    private FridgeController controller;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> ingredientList = new JList<>(listModel);

    private final JTextField inputField = new JTextField(10);
    private final JButton addButton = new JButton("Add");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton backButton = new JButton("Back");

    private Runnable onBack;

    private List<Ingredient> currentIngredients;

    public FridgeView(FridgeViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("My Fridge");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(new JScrollPane(ingredientList));

        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        add(inputPanel);

        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        addButton.addActionListener(e -> {
            String name = inputField.getText().trim();
            if (name.isEmpty()) {
                return;
            }
            try {
                controller.addIngredient(name);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to add ingredient: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            inputField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int idx = ingredientList.getSelectedIndex();
            if (idx < 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select an ingredient to delete.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Ingredient selectedIng = currentIngredients.get(idx);

            try {
                controller.deleteIngredient(selectedIng);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete ingredient: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });
    }

    public void setController(FridgeController controller) {
        this.controller = controller;
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newVal = evt.getNewValue();
        if (newVal instanceof FridgeState) {
            FridgeState state = (FridgeState) newVal;
            updateList(state.getIngredients());
        }
    }

    private void updateList(List<Ingredient> ingredients) {
        this.currentIngredients = ingredients;
        listModel.clear();
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                listModel.addElement(ing.getName());
            }
        }
    }
}
