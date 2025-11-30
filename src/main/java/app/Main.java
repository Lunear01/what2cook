package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point of the What2Cook application.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        // Prevent instantiation.
    }


    /**
     * Launches the What2Cook application.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = RecipeAppBuilder.build();
            frame.setVisible(true);
        });
    }
}

