package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import interface_adapter.ingredient_search.IngredientSearchState;
import interface_adapter.ingredient_search.IngredientSearchViewModel;

public class IngredientSearchView extends JPanel
        implements ActionListener, PropertyChangeListener {

    private final IngredientSearchViewModel viewModel;

    private final JLabel titleLabel = new JLabel("Enter Your Ingredients");
    private final JTextField inputField = new JTextField(20);
    private final JButton addButton = new JButton("Add");
    private final JButton nextButton = new JButton("Search Recipes");
    private final JButton viewFridgeButton = new JButton("View My Fridge");

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> ingredientList = new JList<>(listModel);

    private final JLabel errorLabel = new JLabel(" ");

    private Runnable onNext;
    private Runnable onOpenFridge;
    /** 当成功添加一个 ingredient 时调用，把名字传给外面（FridgeController） */
    private Consumer<String> onAddToFridge;

    public IngredientSearchView(IngredientSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initializeUserInterface();
    }

    private void initializeUserInterface() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final int viewModelBorder = 20;
        setBorder(BorderFactory.createEmptyBorder(
                viewModelBorder, viewModelBorder, viewModelBorder, viewModelBorder));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final float titleLabelSize = 16f;
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, titleLabelSize));

        final JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Ingredient:"));
        inputPanel.add(inputField);
        inputPanel.add(addButton);

        final JScrollPane scrollPane = new JScrollPane(ingredientList);
        final int scrollPaneWidth = 400;
        final int scrollPaneHeight = 200;
        scrollPane.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeight));

        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewFridgeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titleLabel);
        final int height10 = 10;
        add(Box.createVerticalStrut(height10));
        add(inputPanel);
        add(Box.createVerticalStrut(height10));
        add(scrollPane);
        add(Box.createVerticalStrut(height10));
        add(nextButton);
        add(Box.createVerticalStrut(height10));
        add(viewFridgeButton);
        add(Box.createVerticalStrut(height10));
        add(errorLabel);

        addButton.addActionListener(this);
        nextButton.addActionListener(this);
        viewFridgeButton.addActionListener(this);
    }

    public void setOnNext(Runnable onNext) {
        this.onNext = onNext;
    }

    public void setOnOpenFridge(Runnable onOpenFridge) {
        this.onOpenFridge = onOpenFridge;
    }

    /** AppBuilder 用它把 Add 动作连接到 FridgeController */
    public void setOnAddToFridge(Consumer<String> onAddToFridge) {
        this.onAddToFridge = onAddToFridge;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object src = e.getSource();

        if (src == addButton) {
            final String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                final IngredientSearchState oldState = viewModel.getState();
                final IngredientSearchState newState = new IngredientSearchState(oldState);
                newState.getIngredients().add(text);
                newState.setCurrentInput("");
                newState.setError(null);
                viewModel.setState(newState);
                viewModel.firePropertyChanged();

                inputField.setText("");

                // ★ 把新增的 ingredient 同步给 Fridge（通过回调）
                if (onAddToFridge != null) {
                    onAddToFridge.accept(text);
                }
            }
        }
        else if (src == nextButton) {
            if (onNext != null) {
                onNext.run();
            }
        }
        else if (src == viewFridgeButton) {
            if (onOpenFridge != null) {
                onOpenFridge.run();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newVal = evt.getNewValue();

        final IngredientSearchState state = (IngredientSearchState) newVal;

        listModel.clear();
        for (String ing : state.getIngredients()) {
            listModel.addElement(ing);
        }

        final String err = state.getError();
        if (err != null && !err.isEmpty()) {
            errorLabel.setText(err);
        }
        else {
            errorLabel.setText(" ");
        }
    }
}
