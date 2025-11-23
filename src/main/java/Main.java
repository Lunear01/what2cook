import app.login.LoginInteractor;
import app.signup.SignupInteractor;
import dataaccess.UserDataAccesssObject;
import entity.User;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;

import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchViewModel;

import interface_adapter.cookinglist.AddToCookingListController;
import interface_adapter.cookinglist.AddToCookingListPresenter;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.RemoveFromCookingListController;
import interface_adapter.cookinglist.RemoveFromCookingListPresenter;

import app.cookinglist.AddToCookingListInteractor;
import app.cookinglist.RemoveFromCookingListInteractor;

import recipeapi.RecipeFetcher;
import recipeapi.SpoonacularRecipeFetcher;

import view.LoginView;
import view.SignupView;
import view.AppView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Setup data access
            UserDataAccesssObject userDAO = new UserDataAccesssObject();

            // Sample users
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

            // Login Use Case
            LoginViewModel loginViewModel = new LoginViewModel();
            LoginPresenter loginPresenter = new LoginPresenter(loginViewModel);
            LoginInteractor loginInteractor = new LoginInteractor(userDAO, loginPresenter);
            LoginController loginController = new LoginController(loginInteractor);

            // Signup Use Case
            SignupViewModel signupViewModel = new SignupViewModel();
            SignupPresenter signupPresenter = new SignupPresenter(signupViewModel);
            SignupInteractor signupInteractor = new SignupInteractor(userDAO, signupPresenter);
            SignupController signupController = new SignupController(signupInteractor);

            // Login/Signup Frame
            JFrame frame = new JFrame("What2Cook");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 450);
            frame.setLocationRelativeTo(null);

            LoginView loginView = new LoginView(loginViewModel);
            loginView.setLoginController(loginController);
            loginView.setSignupController(signupController);
            loginView.setSignupViewModel(signupViewModel);

            SignupView signupView = new SignupView(signupViewModel);
            signupView.setSignupController(signupController);

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);
            cardPanel.add(loginView, "login");
            cardPanel.add(signupView, "signup");

            loginView.setOnSwitchToSignup(() -> {
                frame.setTitle("What2Cook - Sign Up");
                cardLayout.show(cardPanel, "signup");
            });

            signupView.setOnBackToLogin(() -> {
                frame.setTitle("What2Cook - Login");
                cardLayout.show(cardPanel, "login");
            });

            // ✅ 登录成功 -> 关闭登录窗口 -> 打开主界面
            loginView.setOnLoginSuccess((String username) -> {
                frame.dispose();
                JFrame appFrame = buildAppAfterLogin(userDAO, username);
                appFrame.setVisible(true);
            });

            frame.add(cardPanel);
            frame.setTitle("What2Cook - Login");
            cardLayout.show(cardPanel, "login");
            frame.setVisible(true);
        });
    }

    // ✅ 登录后主界面 wiring（Add + Remove + Search）
    private static JFrame buildAppAfterLogin(UserDataAccesssObject userDAO, String username) {

        // ---- ViewModels ----
        RecipeSearchViewModel recipeSearchVM = new RecipeSearchViewModel();
        CookingListViewModel cookingListVM = new CookingListViewModel();

        // =========================
        // 1) AddToCookingList wiring
        // =========================
        AddToCookingListPresenter addPresenter =
                new AddToCookingListPresenter(cookingListVM);
        AddToCookingListInteractor addInteractor =
                new AddToCookingListInteractor(userDAO, addPresenter);
        AddToCookingListController addController =
                new AddToCookingListController(addInteractor);

        // =============================
        // 2) RemoveFromCookingList wiring
        // =============================
        RemoveFromCookingListPresenter removePresenter =
                new RemoveFromCookingListPresenter(cookingListVM);
        RemoveFromCookingListInteractor removeInteractor =
                new RemoveFromCookingListInteractor(userDAO, removePresenter);
        RemoveFromCookingListController removeController =
                new RemoveFromCookingListController(removeInteractor);

        // =========================
        // 3) RecipeSearch wiring
        // =========================
        RecipeFetcher fetcher = new SpoonacularRecipeFetcher();
        RecipeSearchController recipeSearchController =
                new RecipeSearchController(recipeSearchVM, fetcher);

        return new AppView(
                username,
                recipeSearchVM, recipeSearchController,
                cookingListVM, addController,
                removeController
        );
    }
}
