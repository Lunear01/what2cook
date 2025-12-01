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
    private final JButton backButton = new JButton("Back");

    /** Callback set by RecipeAppBuilder，用来返回到 Ingredient 页面。 */
    private Runnable onBack;

    public FridgeView(FridgeViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("My Fridge");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // List
        add(new JScrollPane(ingredientList));

        // 输入 + Add 按钮
        JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(addButton);
        add(inputPanel);

        // Back 按钮
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        // 监听 Add
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

        // 监听 Back
        backButton.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });
    }

    public void setController(FridgeController controller) {
        this.controller = controller;
    }

    /** 在 RecipeAppBuilder 里调用，用来设置返回动作。 */
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
        listModel.clear();
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                listModel.addElement(ing.getName());
            }
        }
    }
}
