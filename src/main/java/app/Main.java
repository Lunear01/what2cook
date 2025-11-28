package app;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Application entry point.
 */
public final class Main {

    private Main() {
        // prevent instantiation
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 用 RecipeAppBuilder 构建整个应用的 JFrame
                JFrame frame = RecipeAppBuilder.build();
                frame.setVisible(true);
            }
            catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to start What2Cook:\n" + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
