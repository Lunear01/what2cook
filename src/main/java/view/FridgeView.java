package view;

import entity.Ingredient;
import interface_adapter.fridgemodify.FridgeController;
import interface_adapter.fridgemodify.FridgeState;
import interface_adapter.fridgemodify.FridgeViewModel;

import javax.swing.*;
import java.awt.*;
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
        add(inputPanel);

        addButton.addActionListener(e -> {
            String name = inputField.getText();
            if (name.isEmpty()) return;
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
    }

    public void setController(FridgeController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newVal = evt.getNewValue();
        if (newVal != null && newVal instanceof FridgeState) {
            FridgeState state = (FridgeState) newVal;
            updateList(state.getIngredients());
        }
    }

    private void updateList(List<Ingredient> ingredients) {
        listModel.clear();
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                listModel.addElement(ing.getName());
            }
        }
    }
}
