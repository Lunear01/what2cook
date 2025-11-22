package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Login View.
 * - 显示用户名/密码输入框
 * - 点击 Login 调用 LoginController
 * - 点击 Sign Up 触发 onSwitchToSignup 回调（由 Main 里的 CardLayout 切换界面）
 * - 当 LoginViewModel 中 isLoggedIn == true 时，调用 onLoginSuccess 回调
 */
public class LoginView extends JPanel implements ActionListener, PropertyChangeListener {

    private final LoginViewModel loginViewModel;

    private SignupViewModel signupViewModel;
    private LoginController loginController;
    private SignupController signupController;

    // 回调：由 Main 注入，用于切换界面
    private Runnable onSwitchToSignup;
    private Runnable onLoginSuccess;

    // UI 组件
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton signupButton = new JButton("Sign up");
    private final JLabel errorLabel = new JLabel(" ");

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("What2Cook - Login");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("Username:"));
        userPanel.add(usernameField);

        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(new JLabel("Password:"));
        passPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(15));
        add(userPanel);
        add(passPanel);
        add(Box.createVerticalStrut(10));
        add(buttonPanel);
        add(Box.createVerticalStrut(10));
        add(errorLabel);

        loginButton.addActionListener(this);
        signupButton.addActionListener(this);
    }

    /* ========= setters for controllers & callbacks ========= */

    public void setLoginController(LoginController controller) {
        this.loginController = controller;
    }

    public void setSignupController(SignupController controller) {
        this.signupController = controller;
    }

    public void setSignupViewModel(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
    }

    /** 从 Main 注入：切换到 Signup 界面 */
    public void setOnSwitchToSignup(Runnable onSwitchToSignup) {
        this.onSwitchToSignup = onSwitchToSignup;
    }

    /** 从 Main 注入：登录成功后切换到 IngredientSearch 界面 */
    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    /* ================== 事件处理 ================== */

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == loginButton) {
            if (loginController != null) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                // 清空错误提示
                errorLabel.setText(" ");
                loginController.login(username, password);
            }
        } else if (src == signupButton) {
            // 这里只负责通知外面切 view，真正的切换由 Main 里的 CardLayout 完成
            if (onSwitchToSignup != null) {
                onSwitchToSignup.run();
            }
        }
    }

    /* ========== ViewModel 改变时，刷新界面 ========== */

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newVal = evt.getNewValue();
        if (!(newVal instanceof LoginState)) {
            return;
        }
        LoginState state = (LoginState) newVal;

        // 更新文本框（可选）
        usernameField.setText(state.getUsername());
        // 密码一般不回显，这里不设置 passwordField

        // 显示错误信息
        String err = state.getErrorMessage();
        if (err != null && !err.isEmpty()) {
            errorLabel.setText(err);
        } else {
            errorLabel.setText(" ");
        }

        // 如果已经登录成功，通知 Main 切换到下一个界面
        if (state.isLoggedIn() && onLoginSuccess != null) {
            onLoginSuccess.run();
        }
    }
}