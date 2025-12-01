package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import entity.Ingredient;
import interface_adapter.fridgemodify.FridgeController;
import interface_adapter.fridgemodify.FridgeState;
import interface_adapter.fridgemodify.FridgeViewModel;

public class FridgeView extends JPanel implements PropertyChangeListener {

    private static final String ERROR_TITLE = "Error";
    private static final String NO_SELECTION_TITLE = "No Selection";

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

    public FridgeView(final FridgeViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("My Fridge");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(new JScrollPane(ingredientList));

        final JPanel inputPanel = new JPanel();
        inputPanel.add(inputField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        add(inputPanel);

        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backButton);

        // Lambdas only call methods → avoid long lambda body / return issues
        addButton.addActionListener(event -> handleAddIngredient());
        deleteButton.addActionListener(event -> handleDeleteIngredient());
        backButton.addActionListener(event -> handleBack());
    }

    private void handleAddIngredient() {

        final String name = inputField.getText().trim();
        if (name.isEmpty()) {
            return;
        }

        try {
            controller.addIngredient(name);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add ingredient: " + ex.getMessage(),
                    ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
        }

        inputField.setText("");
    }

    private void handleDeleteIngredient() {

        final int index = ingredientList.getSelectedIndex();
        if (index < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an ingredient to delete.",
                    NO_SELECTION_TITLE,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        final Ingredient selectedIngredient = currentIngredients.get(index);

        try {
            controller.deleteIngredient(selectedIngredient);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete ingredient: " + ex.getMessage(),
                    ERROR_TITLE,
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleBack() {
        if (onBack != null) {
            onBack.run();
        }
    }

    public void setController(final FridgeController controller) {
        this.controller = controller;
    }

    public void setOnBack(final Runnable onBack) {
        this.onBack = onBack;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Object newValue = evt.getNewValue();
        if (newValue instanceof FridgeState) {
            final FridgeState state = (FridgeState) newValue;
            updateList(state.getIngredients());
        }
    }

    private void updateList(final List<Ingredient> ingredients) {
        currentIngredients = ingredients;
        listModel.clear();

        if (ingredients != null) {
            for (Ingredient ingredient : ingredients) {
                listModel.addElement(ingredient.getName());
            }
        }
    }
}
