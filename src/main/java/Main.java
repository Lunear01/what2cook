import app.login.LoginInputBoundary;
import app.login.LoginInteractor;
import app.login.LoginUserDataAccessInterface;
import app.signup.SignupInteractor;
import app.signup.SignupUserDataAccessInterface;
import dataaccess.UserDataAccesssObject;
import entity.User;
import entity.UserBuilder;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import view.LoginView;
import view.SignupView;

import javax.swing.*;
import java.awt.*;

/**
 * Main entry point for What2Cook application.
 * Demonstrates the Login and Signup GUI with sample users.
 */
public class Main {
    // currently just signup login gui
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Setup data access
            final UserDataAccesssObject userDAO = new UserDataAccesssObject();

            // Create sample users
            final User user1 = new UserBuilder()
                    .setName("jonathan_calver2")
                    .setPassword("password123")
                    .build();
            final User user2 = new UserBuilder()
                    .setName("david")
                    .setPassword("pass456")
                    .build();

            userDAO.saveUser(user1);
            userDAO.saveUser(user2);

            // Setup Login Use Case
            final LoginViewModel loginViewModel = new LoginViewModel();
            final LoginPresenter loginPresenter = new LoginPresenter(loginViewModel);
            final LoginInteractor loginInteractor = new LoginInteractor(userDAO, loginPresenter);
            final LoginController loginController = new LoginController(loginInteractor);

            // Setup Signup Use Case
            final SignupViewModel signupViewModel = new SignupViewModel();
            final SignupPresenter signupPresenter = new SignupPresenter(signupViewModel);
            final SignupInteractor signupInteractor = new SignupInteractor(userDAO, signupPresenter);
            final SignupController signupController = new SignupController(signupInteractor);

            // Create and setup JFrame
            final JFrame frame = new JFrame("What2Cook");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 450);
            frame.setLocationRelativeTo(null);

            // Setup Views
            final LoginView loginView = new LoginView(loginViewModel);
            loginView.setLoginController(loginController);
            loginView.setSignupController(signupController);
            loginView.setSignupViewModel(signupViewModel);

            final SignupView signupView = new SignupView(signupViewModel);
            signupView.setSignupController(signupController);

            // CardLayout for view switching
            final CardLayout cardLayout = new CardLayout();
            final JPanel cardPanel = new JPanel(cardLayout);
            cardPanel.add(loginView, "login");
            cardPanel.add(signupView, "signup");

            // Setup navigation
            loginView.setOnSwitchToSignup(() -> {
                frame.setTitle("What2Cook - Sign Up");
                cardLayout.show(cardPanel, "signup");
            });

            signupView.setOnBackToLogin(() -> {
                frame.setTitle("What2Cook - Login");
                cardLayout.show(cardPanel, "login");
            });

            frame.add(cardPanel);
            frame.setTitle("What2Cook - Login");
            cardLayout.show(cardPanel, "login");
            frame.setVisible(true);
        });
    }
}
