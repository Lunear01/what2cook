package view;

import interface_adapter.ingredient_search.IngredientSearchController;
import interface_adapter.ingredient_search.IngredientSearchState;
import interface_adapter.ingredient_search.IngredientSearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Ingredient Search View.
 * - 输入 ingredient 名称，点击 Add 添加到列表
 * - 点击 "Search Recipes" 会调用 onNext 回调（由 RecipeAppBuilder 设置），并跳转到 RecipeSearchView
 */
public class IngredientSearchView extends JPanel
        implements ActionListener, PropertyChangeListener {

    private final IngredientSearchViewModel viewModel;

    private final JLabel titleLabel = new JLabel("Enter Your Ingredients");
    private final JLabel inputLabel = new JLabel("Ingredient:");
    private final JTextField inputField = new JTextField(20);
    private final JButton addButton = new JButton("Add");
    private final JTextArea ingredientsArea = new JTextArea(15, 60);
    private final JButton searchButton = new JButton("Search Recipes");

    // 由 RecipeAppBuilder 注入，用于点击 Search 时切换到下一张卡片
    private Runnable onNext;

    // 为了和 RecipeAppBuilder 对上，这里预留 controller（暂时可以不用）
    private IngredientSearchController controller;

    public IngredientSearchView(IngredientSearchViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 输入行
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(addButton);

        // ingredients 文本区域
        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(ingredientsArea);

        // Search 按钮
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        int gap = 10;
        add(Box.createVerticalStrut(gap));
        add(titleLabel);
        add(Box.createVerticalStrut(gap));
        add(inputPanel);
        add(Box.createVerticalStrut(gap));
        add(scrollPane);
        add(Box.createVerticalStrut(gap));
        add(searchButton);
        add(Box.createVerticalStrut(gap));

        // listeners
        addButton.addActionListener(this);
        searchButton.addActionListener(this);
    }

    /**
     * 由外部（RecipeAppBuilder）注入 controller。
     */
    public void setController(IngredientSearchController controller) {
        this.controller = controller;
    }

    /**
     * 由外部（RecipeAppBuilder）注入，当用户点击 "Search Recipes" 时调用。
     */
    public void setOnNext(Runnable onNext) {
        this.onNext = onNext;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == addButton) {
            handleAddIngredient();
        } else if (src == searchButton) {
            if (onNext != null) {
                onNext.run();
            }
        }
    }

    private void handleAddIngredient() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        IngredientSearchState oldState = viewModel.getState();
        IngredientSearchState newState = new IngredientSearchState(oldState);

        List<String> list = new ArrayList<>(newState.getIngredients());
        list.add(text);
        newState.setIngredients(list);
        newState.setCurrentInput("");

        viewModel.setState(newState); // 会触发 propertyChange
        inputField.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newVal = evt.getNewValue();
        if (newVal instanceof IngredientSearchState) {
            IngredientSearchState state = (IngredientSearchState) newVal;
            updateFromState(state);
        }
    }

    private void updateFromState(IngredientSearchState state) {
        // 更新文本框中显示的 ingredients
        List<String> ingredients = state.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            ingredientsArea.setText("");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : ingredients) {
                sb.append(s).append("\n");
            }
            ingredientsArea.setText(sb.toString());
        }
    }
}
