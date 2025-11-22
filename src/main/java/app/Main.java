package app;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dataaccess.UserDataAccesssObject;
import entity.Ingredient;
import entity.User;
import entity.UserBuilder;
import interface_adapter.ingredient_search.IngredientSearchViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginInteractor;
import use_case.signup.SignupInteractor;
import view.IngredientSearchView;
import view.LoginView;
import view.RecipeSearchView;
import view.SignupView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // In memory version
            final UserDataAccesssObject userDAO = new UserDataAccesssObject();

            // Local storage version
            // final FileUserDataAccesssObject userDAO = new FileUserDataAccesssObject();

            // Create sample users
            final User user1 = new UserBuilder()
                    .withName("jonathan_calver2")
                    .withPassword("password123")
                    .withEmail("39485@adf.com")
                    .build();
            final User user2 = new UserBuilder()
                    .withName("david")
                    .withPassword("pass456")
                    .withEmail("dkh.kim@mail.utoronto.com")
                    .build();

            userDAO.save(user1);
            userDAO.save(user2);
            // --- login setup ---
            final LoginViewModel loginViewModel = new LoginViewModel();
            final LoginPresenter loginPresenter = new LoginPresenter(loginViewModel);
            final LoginInteractor loginInteractor = new LoginInteractor(userDAO, loginPresenter);
            final LoginController loginController = new LoginController(loginInteractor);

            // --- signup setup ---
            final SignupViewModel signupViewModel = new SignupViewModel();
            final SignupPresenter signupPresenter = new SignupPresenter(signupViewModel);
            final SignupInteractor signupInteractor = new SignupInteractor(userDAO, signupPresenter);
            final SignupController signupController = new SignupController(signupInteractor);

            // --- ingredient search setup ---
            final IngredientSearchViewModel ingredientSearchViewModel = new IngredientSearchViewModel();
            final IngredientSearchView ingredientSearchView = new IngredientSearchView(ingredientSearchViewModel);

            // --- recipe search setup ---
            final RecipeSearchViewModel recipeSearchViewModel = new RecipeSearchViewModel();
            final RecipeSearchView recipeSearchView = new RecipeSearchView(recipeSearchViewModel);
            final RecipeSearchController recipeSearchController =
                    new RecipeSearchController(recipeSearchViewModel);
            recipeSearchView.setController(recipeSearchController);

            // --- main frame ---
            final JFrame frame = new JFrame("What2Cook");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 450);
            frame.setLocationRelativeTo(null);

            // --- views ---
            final LoginView loginView = new LoginView(loginViewModel);
            loginView.setLoginController(loginController);
            loginView.setSignupController(signupController);
            loginView.setSignupViewModel(signupViewModel);

            final SignupView signupView = new SignupView(signupViewModel);
            signupView.setSignupController(signupController);

            final CardLayout cardLayout = new CardLayout();
            final JPanel cardPanel = new JPanel(cardLayout);

            cardPanel.add(loginView, "login");
            cardPanel.add(signupView, "signup");
            cardPanel.add(ingredientSearchView, "ingredient");
            cardPanel.add(recipeSearchView, "recipe");

            // --- navigation ---
            loginView.setOnSwitchToSignup(() -> {
                frame.setTitle("What2Cook - Sign Up");
                cardLayout.show(cardPanel, "signup");
            });

            signupView.setOnBackToLogin(() -> {
                frame.setTitle("What2Cook - Login");
                cardLayout.show(cardPanel, "login");
            });

            loginView.setOnLoginSuccess(() -> {
                frame.setTitle("What2Cook - Ingredients");
                cardLayout.show(cardPanel, "ingredient");
            });

            // --- core logic: go from IngredientSearch → RecipeSearch ---
            ingredientSearchView.setOnNext(() -> {
                // 1. 从 IngredientSearchViewModel 取得用户输入的食材名（String）
                final List<String> names = ingredientSearchViewModel.getState().getIngredients();

                // 2. 转成 Ingredient 对象形式（RecipeSearch 使用这个）
                final List<Ingredient> ingredients = new ArrayList<>();
                for (String name : names) {
                    ingredients.add(new Ingredient(name));
                }

                // 3. 把 ingredients 放进 RecipeSearchViewModel → 让 RecipeSearchView 显示
                recipeSearchViewModel.setCurrentIngredients(ingredients);

                // 4. 调用 controller 搜索（现在用的是 demo 数据）
                recipeSearchController.searchByIngredients(ingredients);

                // 5. 切到 recipe 界面
                frame.setTitle("What2Cook - Recipes");
                cardLayout.show(cardPanel, "recipe");
            });

            frame.add(cardPanel);
            frame.setTitle("What2Cook - Login");
            cardLayout.show(cardPanel, "login");
            frame.setVisible(true);
        });
    }
}
