package view;

import interface_adapter.fridgemodify.FridgeController;
import interface_adapter.fridgemodify.FridgeState;
import interface_adapter.fridgemodify.FridgeViewModel;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * A simple page-style view to display the user's fridge contents.
 * This is meant to be used as one card inside the main CardLayout.
 */
public class FridgeView extends JPanel implements PropertyChangeListener {

    private final FridgeViewModel viewModel;

    private FridgeController controller;
    private Runnable onBack;

    private final JLabel titleLabel = new JLabel("My Fridge");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> ingredientList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton backButton = new JButton("Back");

    public FridgeView(FridgeViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(ingredientList);

        add(titleLabel);
        add(scrollPane);
        add(refreshButton);
        add(backButton);

        // 刷新按钮：调用 use case 获取当前用户的冰箱内容
        refreshButton.addActionListener(event -> refreshFridge());

        // 返回按钮：交给外面通过 onBack 控制（比如回到 Ingredient 页面）
        backButton.addActionListener(event -> {
            if (onBack != null) {
                onBack.run();
            }
        });
    }

    /**
     * 调用 controller 的 GetIngredient，用当前登录用户刷新冰箱内容。
     */
    private void refreshFridge() {
        if (controller == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fridge controller is not configured.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        try {
            controller.GetIngredient();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to refresh fridge: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * ViewModel 的状态变化时被调用。
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue instanceof FridgeState) {
            FridgeState state = (FridgeState) newValue;

            updateList(state.getIngredients());

            String error = state.getErrorMessage();
            if (error != null && !error.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        error,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void updateList(List<String> ingredients) {
        listModel.clear();
        if (ingredients != null) {
            for (String name : ingredients) {
                listModel.addElement(name);
            }
        }
    }

    // --- wiring ---

    /** 由 RecipeAppBuilder 传进来 FridgeController */
    public void setController(FridgeController controller) {
        this.controller = controller;
    }

    /** 设置返回时要做的事情（比如切回 ingredient 卡片） */
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}
