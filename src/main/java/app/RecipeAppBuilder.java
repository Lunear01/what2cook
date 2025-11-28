package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataaccess.IngredientDataAccessImpl;
import dataaccess.RecipeDataAccessImpl;
import dataaccess.UserDataAccessImpl;
import entity.Ingredient;
import interface_adapter.ingredient_search.IngredientSearchController;
import interface_adapter.ingredient_search.IngredientSearchPresenter;
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
import use_case.ingredient_search.IngredientSearchInputBoundary;
import use_case.ingredient_search.IngredientSearchInteractor;
import use_case.ingredient_search.IngredientSearchOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserDataAccessInterface;
import use_case.recipe_search.RecipeSearchInputBoundary;
import use_case.recipe_search.RecipeSearchInteractor;
import use_case.recipe_search.RecipeSearchOutputBoundary;
import use_case.signup.EmailValidation;
import use_case.signup.EmailValidationImpl;
import use_case.signup.PasswordValidation;
import use_case.signup.PasswordValidationImpl;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupUserDataAccessInterface;
import view.IngredientSearchView;
import view.LoginView;
import view.RecipeSearchView;
import view.SignupView;

/**
 * Builds the What2Cook application frame, wiring all views, controllers,
 * presenters, interactors, and data access objects.
 */
public final class RecipeAppBuilder {

    private RecipeAppBuilder() {
        // prevent instantiation
    }

    /**
     * Builds the main JFrame for the application.
     *
     * @return configured JFrame, ready to be setVisible(true)
     * @throws Exception if any wiring-related initialization fails
     */
    public static JFrame build() throws Exception {

        // ===== Data Access =====
        final UserDataAccessImpl userDAO = new UserDataAccessImpl();
        final IngredientDataAccessImpl ingredientDAO = new IngredientDataAccessImpl();
        final RecipeDataAccessImpl recipeDAO = new RecipeDataAccessImpl(); // 目前只构造，后面可以扩展使用

        // Remote recipe fetcher (Spoonacular + caching)
        final RecipeFetcher fetcher =
                new CachingRecipeFetcher(new SpoonacularRecipeFetcher());

        // ===== ViewModels =====
        final LoginViewModel loginViewModel = new LoginViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();
        final IngredientSearchViewModel ingredientSearchViewModel = new IngredientSearchViewModel();
        final RecipeSearchViewModel recipeSearchViewModel = new RecipeSearchViewModel();

        // ===== CardLayout root panel =====
        final CardLayout cardLayout = new CardLayout();
        final JPanel cardPanel = new JPanel(cardLayout);

        // ----- Views -----
        final LoginView loginView = new LoginView(loginViewModel);
        final SignupView signupView = new SignupView(signupViewModel);
        final IngredientSearchView ingredientSearchView =
                new IngredientSearchView(ingredientSearchViewModel);
        final RecipeSearchView recipeSearchView =
                new RecipeSearchView(recipeSearchViewModel);

        // 把各个 view 注册到 CardLayout
        cardPanel.add(loginView, LoginViewModel.VIEW_NAME);
        cardPanel.add(signupView, SignupViewModel.VIEW_NAME);
        cardPanel.add(ingredientSearchView, IngredientSearchViewModel.VIEW_NAME);
        cardPanel.add(recipeSearchView, RecipeSearchViewModel.VIEW_NAME);

        // =====================================================
        //                  Login Use Case
        // =====================================================
        final LoginOutputBoundary loginPresenter =
                new LoginPresenter(loginViewModel);
        final LoginUserDataAccessInterface loginUserDataAccess = userDAO;
        final LoginInputBoundary loginInteractor =
                new LoginInteractor(loginUserDataAccess, loginPresenter);
        final LoginController loginController =
                new LoginController(loginInteractor);

        loginView.setLoginController(loginController);

        // =====================================================
        //                  Signup Use Case
        // =====================================================
        final SignupOutputBoundary signupPresenter =
                new SignupPresenter(signupViewModel);
        final SignupUserDataAccessInterface signupUserDataAccess = userDAO;
        final EmailValidation emailValidation = new EmailValidationImpl();
        final PasswordValidation passwordValidation = new PasswordValidationImpl();

        final SignupInputBoundary signupInteractor =
                new SignupInteractor(
                        signupUserDataAccess,
                        signupPresenter,
                        emailValidation,
                        passwordValidation
                );

        final SignupController signupController =
                new SignupController(signupInteractor);

        signupView.setSignupController(signupController);

        // =====================================================
        //              Ingredient Search Use Case
        // =====================================================
        final IngredientSearchOutputBoundary ingredientSearchPresenter =
                new IngredientSearchPresenter(ingredientSearchViewModel);

        final IngredientSearchInputBoundary ingredientSearchInteractor =
                new IngredientSearchInteractor(ingredientDAO, ingredientSearchPresenter);

        final IngredientSearchController ingredientSearchController =
                new IngredientSearchController(ingredientSearchInteractor);

        ingredientSearchView.setController(ingredientSearchController);

        // =====================================================
        //               Recipe Search Use Case
        // =====================================================
        final RecipeSearchOutputBoundary recipeSearchPresenter =
                new RecipeSearchPresenter(recipeSearchViewModel);

        final RecipeSearchInputBoundary recipeSearchInteractor =
                new RecipeSearchInteractor(fetcher, recipeSearchPresenter);

        final RecipeSearchController recipeSearchController =
                new RecipeSearchController(recipeSearchInteractor);

        recipeSearchView.setController(recipeSearchController);

        // =====================================================
        //                 View Transitions
        // =====================================================

        // Login -> Signup
        loginView.setSignupViewModel(signupViewModel);
        loginView.setSignupController(signupController);
        loginView.setOnSwitchToSignup(() ->
                cardLayout.show(cardPanel, SignupViewModel.VIEW_NAME));

        // Signup -> back to Login
        signupView.setOnBackToLogin(() ->
                cardLayout.show(cardPanel, LoginViewModel.VIEW_NAME));

        // Login success -> Ingredient Search
        loginView.setOnLoginSuccess(() ->
                cardLayout.show(cardPanel, IngredientSearchViewModel.VIEW_NAME));

        // IngredientSearch 点击 "Search Recipes" -> RecipeSearch
        ingredientSearchView.setOnNext(() -> {
            // 从 IngredientSearchViewModel 取出当前原料字符串列表
            final List<String> names =
                    ingredientSearchViewModel.getState().getIngredients();

            // 转成 Ingredient entity 列表（id 先用 -1 占位）
            final List<Ingredient> ingredients = new ArrayList<>();
            for (String name : names) {
                ingredients.add(
                        Ingredient.builder()
                                .setName(name)
                                .setId(-1)
                                .build()
                );
            }

            // 更新 RecipeSearchViewModel 里的当前 ingredients
            recipeSearchViewModel.setCurrentIngredients(ingredients);

            // 触发 use case 调 API 搜索 recipe
            recipeSearchController.searchByIngredients(ingredients);

            // 切换到 RecipeSearch 视图
            cardLayout.show(cardPanel, RecipeSearchViewModel.VIEW_NAME);
        });

        // =====================================================
        //                     JFrame
        // =====================================================
        final JFrame frame = new JFrame("What2Cook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(cardPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // 初始显示 Login 页
        cardLayout.show(cardPanel, LoginViewModel.VIEW_NAME);

        return frame;
    }
}
