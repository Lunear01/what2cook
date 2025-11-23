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

    public SignupView(final SignupViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        initializeLayout();
        final JPanel inputPanel = createInputPanel();
        final JPanel buttonPanel = createButtonPanel();
        assembleView(inputPanel, buttonPanel);
    }

    /**
     * Sets up layout, border, and label styles.
     */
    private void initializeLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final int border = 20;
        setBorder(BorderFactory.createEmptyBorder(border, border, border, border));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final int fontSize = 20;
        titleLabel.setFont(new Font("Arial", Font.BOLD, fontSize));

        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setForeground(Color.RED);
    }

    /**
     * Creates and configures the input panel for the signup form.
     *
     * @return the configured input panel.
     */
    private JPanel createInputPanel() {
        final JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        final int inputPanelWidth = 300;
        final int inputPanelHeight = 300;
        inputPanel.setMaximumSize(new Dimension(inputPanelWidth, inputPanelHeight));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        final int fieldWidth = 300;
        final int fieldHeight = 30;
        usernameField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));

        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));

        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));

        confirmPasswordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmPasswordField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));

        final int height10 = 10;
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(Box.createVerticalStrut(height10));
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(Box.createVerticalStrut(height10));
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(Box.createVerticalStrut(height10));
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);

        return inputPanel;
    }

    /**
     * Creates and configures the button panel with actions.
     *
     * @return the configured button panel.
     */
    private JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        signupButton.addActionListener(ide -> handleSignup());
        backButton.addActionListener(ide -> handleBack());

        final int height10 = 10;
        buttonPanel.add(signupButton);
        buttonPanel.add(Box.createHorizontalStrut(height10));
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    /**
     * Assembles all components into the main view.
     *
     * @param inputPanel  the input panel.
     * @param buttonPanel the button panel.
     */
    private void assembleView(final JPanel inputPanel, final JPanel buttonPanel) {
        final int height10 = 10;
        final int height20 = 20;

        add(titleLabel);
        add(Box.createVerticalStrut(height20));
        add(errorLabel);
        add(Box.createVerticalStrut(height10));
        add(inputPanel);
        add(Box.createVerticalStrut(height20));
        add(buttonPanel);
        add(Box.createVerticalGlue());
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
            }
            else {
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
