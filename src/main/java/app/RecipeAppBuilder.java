package app;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataaccess.UserDataAccesssObject;
import entity.Ingredient;
import entity.User;
import entity.UserBuilder;
import interface_adapter.ingredient_search.IngredientSearchViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.recipe_search.RecipeSearchController;
import interface_adapter.recipe_search.RecipeSearchPresenter;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import recipeapi.CachingRecipeFetcher;
import recipeapi.RecipeFetcher;
import recipeapi.SpoonacularRecipeFetcher;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.recipe_search.RecipeSearchInputBoundary;
import use_case.recipe_search.RecipeSearchInteractor;
import use_case.signup.*;
import view.IngredientSearchView;
import view.LoginView;
import view.RecipeSearchView;
import view.SignupView;

public final class RecipeAppBuilder {

    private RecipeAppBuilder() {
        // Prevent instantiation.
    }

    /**
     * Builds and returns the main application JFrame.
     *
     * @return a fully wired JFrame ready for display.
     */
    public static JFrame build() {

        // --- Data access (gateway) ---
        final UserDataAccesssObject userDao = new UserDataAccesssObject();

        // --- Demo users ---
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

        userDao.save(user1);
        userDao.save(user2);

        // --- Login wiring ---
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoginPresenter loginPresenter = new LoginPresenter(loginViewModel);
        final LoginInputBoundary loginInteractor =
                new LoginInteractor(userDao, loginPresenter);
        final LoginController loginController =
                new LoginController(loginInteractor);

        // --- Signup wiring ---
        final SignupViewModel signupViewModel = new SignupViewModel();
        final SignupPresenter signupPresenter = new SignupPresenter(signupViewModel);
        final EmailValidation emailValidator = new JmailValidationService();
        final PasswordValidation passwordValidator = new PasswordValidationService();
        final SignupInputBoundary signupInteractor =
                new SignupInteractor(userDao, signupPresenter, emailValidator, passwordValidator);
        final SignupController signupController =
                new SignupController(signupInteractor);

        // --- Ingredient search wiring ---
        final IngredientSearchViewModel ingredientSearchViewModel =
                new IngredientSearchViewModel();
        final IngredientSearchView ingredientSearchView =
                new IngredientSearchView(ingredientSearchViewModel);

        // --- Recipe search wiring (Clean Architecture) ---
        final RecipeSearchViewModel recipeSearchViewModel =
                new RecipeSearchViewModel();
        final RecipeSearchPresenter recipeSearchPresenter =
                new RecipeSearchPresenter(recipeSearchViewModel);

        final RecipeFetcher fetcher = new CachingRecipeFetcher(new SpoonacularRecipeFetcher());
        final RecipeSearchInputBoundary recipeSearchInteractor =
                new RecipeSearchInteractor(fetcher, recipeSearchPresenter);

        final RecipeSearchController recipeSearchController =
                new RecipeSearchController(recipeSearchInteractor);

        final RecipeSearchView recipeSearchView =
                new RecipeSearchView(recipeSearchViewModel);
        recipeSearchView.setController(recipeSearchController);

        // --- Frame and card layout ---
        final JFrame frame = new JFrame("What2Cook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final int frameWidth = 500;
        final int frameHeight = 450;
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);

        final CardLayout cardLayout = new CardLayout();
        final JPanel cardPanel = new JPanel(cardLayout);

        // --- Views ---
        final LoginView loginView = new LoginView(loginViewModel);
        loginView.setLoginController(loginController);
        loginView.setSignupController(signupController);
        loginView.setSignupViewModel(signupViewModel);

        final SignupView signupView = new SignupView(signupViewModel);
        signupView.setSignupController(signupController);

        final String login = "login";
        final String signup = "signup";
        final String ingredient = "ingredient";
        final String recipe = "recipe";

        cardPanel.add(loginView, login);
        cardPanel.add(signupView, signup);
        cardPanel.add(ingredientSearchView, ingredient);
        cardPanel.add(recipeSearchView, recipe);

        // --- Navigation wiring ---
        loginView.setOnSwitchToSignup(() -> {
            frame.setTitle("What2Cook - Sign Up");
            cardLayout.show(cardPanel, signup);
        });

        signupView.setOnBackToLogin(() -> {
            frame.setTitle("What2Cook - Login");
            cardLayout.show(cardPanel, login);
        });

        loginView.setOnLoginSuccess(() -> {
            frame.setTitle("What2Cook - Ingredients");
            cardLayout.show(cardPanel, ingredient);
        });

        // --- Ingredient → Recipe flow ---
        ingredientSearchView.setOnNext(() -> {
            final List<String> names =
                    ingredientSearchViewModel.getState().getIngredients();

            final List<Ingredient> ingredients = new ArrayList<>();
            for (String name : names) {
                ingredients.add(new Ingredient(name, -1));
            }

            recipeSearchViewModel.setCurrentIngredients(ingredients);
            recipeSearchController.searchByIngredients(ingredients);

            frame.setTitle("What2Cook - Recipes");
            cardLayout.show(cardPanel, recipe);
        });

        frame.add(cardPanel);
        frame.setTitle("What2Cook - Login");
        cardLayout.show(cardPanel, login);

        return frame;
    }
}
