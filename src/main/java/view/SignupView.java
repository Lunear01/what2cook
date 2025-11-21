package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Signup View GUI.
 * Allows users to create a new account.
 */
public class SignupView extends JPanel implements PropertyChangeListener {

    private final SignupViewModel viewModel;
    private SignupController signupController;
    private Runnable onBackToLogin;

    // UI Components
    private final JLabel titleLabel = new JLabel("Create Account");
    private final JLabel usernameLabel = new JLabel("Username:");
    private final JTextField usernameField = new JTextField(20);
    private final JLabel passwordLabel = new JLabel("Password:");
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JButton signupButton = new JButton("Sign Up");
    private final JButton backButton = new JButton("Back to Login");
    private final JLabel errorLabel = new JLabel("");

    public SignupView(SignupViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        // ===== Layout Setup =====
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Error label
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setForeground(Color.RED);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setMaximumSize(new Dimension(300, 250));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(300, 30));

        // Password field
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(300, 30));

        // Confirm password field
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordField.setMaximumSize(new Dimension(300, 30));

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.addActionListener(e -> handleSignup());
        backButton.addActionListener(e -> handleBack());
        buttonPanel.add(signupButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(backButton);

        // ===== Add Components =====
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(20));
        this.add(errorLabel);
        this.add(Box.createVerticalStrut(10));
        this.add(inputPanel);
        this.add(Box.createVerticalStrut(20));
        this.add(buttonPanel);
        this.add(Box.createVerticalGlue());
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (signupController != null) {
            signupController.signup(username, password, confirmPassword);
        }
    }

    private void handleBack() {
        clearFields();
        errorLabel.setText("");
        if (onBackToLogin != null) {
            onBackToLogin.run();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newState = evt.getNewValue();
        if (newState instanceof SignupState) {
            final SignupState state = (SignupState) newState;

            if (!state.getErrorMessage().isEmpty()) {
                errorLabel.setText(state.getErrorMessage());
                errorLabel.setForeground(Color.RED);
            } else {
                errorLabel.setText("");
            }

            if (state.isCreated()) {
                JOptionPane.showMessageDialog(this,
                        "Account created successfully! You can now log in.",
                        "Signup Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                // Automatically go back to login after successful signup
                if (onBackToLogin != null) {
                    onBackToLogin.run();
                }
            }
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        errorLabel.setText("");
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }

    public void setOnBackToLogin(Runnable onBackToLogin) {
        this.onBackToLogin = onBackToLogin;
    }
}
