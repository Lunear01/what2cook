package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

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
    private final JLabel emailLabel = new JLabel("Email:");
    private final JTextField emailField = new JTextField(20);
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
        final int borderTop = 20;
        final int borderBottom = 20;
        final int borderLeft = 20;
        final int borderRight = 20;
        this.setBorder(BorderFactory.createEmptyBorder(borderTop, borderLeft, borderBottom, borderRight));

        // Title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final int fontSize = 20;
        titleLabel.setFont(new Font("Arial", Font.BOLD, fontSize));

        // Error label
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setForeground(Color.RED);

        // Input panel
        final JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        final int inputPanelWidth = 300;
        final int inputPanelHeight = 300;
        inputPanel.setMaximumSize(new Dimension(inputPanelWidth, inputPanelHeight));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final int usernameLabelWidth = 300;
        final int usernameLabelHeight = 30;
        usernameField.setMaximumSize(new Dimension(usernameLabelWidth, usernameLabelHeight));

        // Email field
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final int emailLabelWidth = 300;
        final int emailLabelHeight = 30;
        emailField.setMaximumSize(new Dimension(emailLabelWidth, emailLabelHeight));

        // Password field
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final int passwordLabelWidth = 300;
        final int passwordLabelHeight = 30;
        passwordField.setMaximumSize(new Dimension(passwordLabelWidth, passwordLabelHeight));

        // Confirm password field
        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final int confirmPasswordLabelWidth = 300;
        final int confirmPasswordLabelHeight = 30;
        confirmPasswordField.setMaximumSize(new Dimension(confirmPasswordLabelWidth, confirmPasswordLabelHeight));

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        final int height10 = 10;
        final int height20 = 20;
        inputPanel.add(Box.createVerticalStrut(height10));
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(Box.createVerticalStrut(height10));
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(Box.createVerticalStrut(height10));
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);

        // Button panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.addActionListener(e -> handleSignup());
        backButton.addActionListener(e -> handleBack());
        buttonPanel.add(signupButton);
        buttonPanel.add(Box.createHorizontalStrut(height10));
        buttonPanel.add(backButton);

        // ===== Add Components =====
        this.add(titleLabel);
        this.add(Box.createVerticalStrut(height20));
        this.add(errorLabel);
        this.add(Box.createVerticalStrut(height10));
        this.add(inputPanel);
        this.add(Box.createVerticalStrut(height20));
        this.add(buttonPanel);
        this.add(Box.createVerticalGlue());
    }

    private void handleSignup() {
        final String username = usernameField.getText();
        final String email = emailField.getText();
        final String password = new String(passwordField.getPassword());
        final String confirmPassword = new String(confirmPasswordField.getPassword());

        if (signupController != null) {
            signupController.signup(username, email, password, confirmPassword);
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
        final Object newState = evt.getNewValue();
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
