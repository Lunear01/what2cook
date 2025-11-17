package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Simple Login View GUI.
 * Allows user to enter username and password to authenticate.
 */
public class LoginView extends JPanel implements PropertyChangeListener {

    private final LoginViewModel viewModel;
    private LoginController loginController;

    // UI Components
    private final JLabel titleLabel = new JLabel("Welcome to What2Cook");
    private final JLabel usernameLabel = new JLabel("Username:");
    private final JTextField usernameField = new JTextField(20);
    private final JLabel passwordLabel = new JLabel("Password:");
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton signupButton = new JButton("Sign Up");
    private final JLabel errorLabel = new JLabel("");

    public LoginView(LoginViewModel viewModel) {
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
        inputPanel.setMaximumSize(new Dimension(300, 150));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(300, 30));

        // Password field
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(300, 30));

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        signupButton.addActionListener(e -> handleSignup());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(signupButton);

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

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (loginController != null) {
            loginController.login(username, password);
        }
    }

    private void handleSignup() {
        JOptionPane.showMessageDialog(this,
                "Sign up feature coming soon!",
                "Sign Up", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LoginState state = (LoginState) evt.getNewValue();

        // Update error message display
        if (!state.getErrorMessage().isEmpty()) {
            errorLabel.setText(state.getErrorMessage());
            errorLabel.setForeground(Color.RED);
        } else {
            errorLabel.setText("");
        }

        // If login was successful
        if (state.isLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "Login successful! Welcome, " + state.getUsername() + "!",
                    "Login Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }
}

