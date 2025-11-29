package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final LoginViewModel loginViewModel;

    private SignupViewModel signupViewModel;
    private LoginController loginController;
    private SignupController signupController;

    private Runnable onSwitchToSignup;
    private Runnable onLoginSuccess;

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton signupButton = new JButton("Sign up");
    private final JLabel errorLabel = new JLabel(" ");

    public LoginView(final LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        initializeLoginView();
    }

    /**
     * Initializes the layout and components for the Login view.
     */
    private void initializeLoginView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final int loginViewBorder = 20;
        setBorder(BorderFactory.createEmptyBorder(
                loginViewBorder, loginViewBorder, loginViewBorder, loginViewBorder));

        final JLabel title = new JLabel("What2Cook - Login");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        final float titleSize = 18f;
        title.setFont(title.getFont().deriveFont(Font.BOLD, titleSize));

        final JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("Username:"));
        userPanel.add(usernameField);

        final JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(new JLabel("Password:"));
        passPanel.add(passwordField);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final int height15 = 15;
        final int height10 = 10;

        add(title);
        add(Box.createVerticalStrut(height15));
        add(userPanel);
        add(passPanel);
        add(Box.createVerticalStrut(height10));
        add(buttonPanel);
        add(Box.createVerticalStrut(height10));
        add(errorLabel);

        loginButton.addActionListener(this);
        signupButton.addActionListener(this);
    }

    /* setters for controllers & callbacks*/

    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }

    public void setSignupViewModel(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
    }

    /**
     * Sets the callback to be executed when the user chooses to switch to the
     * signup view. This callback is typically injected from the Main class.
     *
     * @param onSwitchToSignup the action to run when switching to the signup view
     */
    public void setOnSwitchToSignup(Runnable onSwitchToSignup) {
        this.onSwitchToSignup = onSwitchToSignup;
    }

    /**
     * Sets the callback to be executed after a successful login, typically used
     * to switch to the IngredientSearch view. This callback is injected from Main.
     *
     * @param onLoginSuccess the action to run upon successful login
     */
    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    /* ================== 事件处理 ================== */

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object src = e.getSource();

        if (src == loginButton) {
            if (loginController != null) {
                final String username = usernameField.getText().trim();
                final String password = new String(passwordField.getPassword());
                // 清空错误提示
                errorLabel.setText(" ");
                loginController.login(username, password);
            }
        }
        else if (src == signupButton) {
            // 这里只负责通知外面切 view，真正的切换由 Main 里的 CardLayout 完成
            if (onSwitchToSignup != null) {
                onSwitchToSignup.run();
            }
        }
    }

    /* ========== ViewModel 改变时，刷新界面 ========== */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        final Object newVal = evt.getNewValue();

        if (newVal instanceof LoginState) {
            final LoginState state = (LoginState) newVal;

            // 更新文本框（可选）
            usernameField.setText(state.getUsername());
            // 密码一般不回显，不设置 passwordField

            // 显示错误信息
            final String err = state.getErrorMessage();
            if (err != null && !err.isEmpty()) {
                errorLabel.setText(err);
            }
            else {
                errorLabel.setText(" ");
            }

            // 如果已登录成功，通知 Main 切换界面
            if (state.isLoggedIn() && onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        }

        // 没有 return —— Checkstyle OK
    }
}

