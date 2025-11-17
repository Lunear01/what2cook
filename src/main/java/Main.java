import app.login.LoginInputBoundary;
import app.login.LoginInteractor;
import app.login.LoginUserDataAccessInterface;
import dataaccess.UserDataAccesssObject;
import entity.User;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import view.LoginView;

import javax.swing.*;

/**
 * Main entry point for What2Cook application.
 * Demonstrates the Login GUI with sample users.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Setup data access
            UserDataAccesssObject userDAO = new UserDataAccesssObject();

            // Create sample users
            User user1 = new User.UserBuilder()
                    .setName("jonathan_calver2")
                    .setPassword("password123")
                    .build();
            User user2 = new User.UserBuilder()
                    .setName("david")
                    .setPassword("pass456")
                    .build();

            userDAO.saveUser(user1);
            userDAO.saveUser(user2);

            // Setup Login Use Case
            LoginViewModel loginViewModel = new LoginViewModel();
            LoginPresenter loginPresenter = new LoginPresenter(loginViewModel);
            LoginInteractor loginInteractor = new LoginInteractor(userDAO, loginPresenter);
            LoginController loginController = new LoginController(loginInteractor);

            // Setup View
            LoginView loginView = new LoginView(loginViewModel);
            loginView.setLoginController(loginController);

            // Create and setup JFrame
            JFrame frame = new JFrame("What2Cook - Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(loginView);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
