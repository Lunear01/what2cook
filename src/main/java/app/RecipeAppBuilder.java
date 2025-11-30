package app;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataaccess.InMemoryFavoriteRecipeDataAccess;
import dataaccess.IngredientDataAccessInterface;
import dataaccess.IngredientDataAccessObject;
import dataaccess.RecipeDataAccessObject;
import dataaccess.UserDataAccesssObject;
import entity.Ingredient;
import interface_adapter.cookinglist.AddToCookingListController;
import interface_adapter.cookinglist.AddToCookingListPresenter;
import interface_adapter.cookinglist.CookingListViewModel;
import interface_adapter.cookinglist.SortCookingListController;
import interface_adapter.cookinglist.SortCookingListPresenter;
import interface_adapter.favoritelist.AddFavoriteRecipeController;
import interface_adapter.favoritelist.AddFavoriteRecipePresenter;
import interface_adapter.favoritelist.FavoriteListViewModel;
import interface_adapter.fridgemodify.FridgeController;
import interface_adapter.fridgemodify.FridgePresenter;
import interface_adapter.fridgemodify.FridgeViewModel;
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
import use_case.cookinglist.AddToCookingList.AddToCookingListInputBoundary;
import use_case.cookinglist.AddToCookingList.AddToCookingListInteractor;
import use_case.cookinglist.AddToCookingList.AddToCookingListOutputBoundary;
import use_case.cookinglist.RecipeDataAccessInterface;
import use_case.cookinglist.SortCookingList.SortCookingListInputBoundary;
import use_case.cookinglist.SortCookingList.SortCookingListInteractor;
import use_case.cookinglist.SortCookingList.SortCookingListOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.recipe_search.RecipeSearchInputBoundary;
import use_case.recipe_search.RecipeSearchInteractor;
import use_case.signup.EmailValidation;
import use_case.signup.JmailValidationService;
import use_case.signup.PasswordValidation;
import use_case.signup.PasswordValidationService;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import view.CookingListView;
import view.FavoriteListView;
import view.FridgeView;
import view.IngredientSearchView;
import view.LoginView;
import view.RecipeInstructionView;
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

        // Fridge data access for fridge use cases
        final IngredientDataAccessInterface ingredientDataAccess =
                new IngredientDataAccessObject();

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

        // --- Cooking list wiring ---
        final CookingListViewModel cookingListViewModel =
                new CookingListViewModel();

        final AddToCookingListOutputBoundary cookingListPresenter =
                new AddToCookingListPresenter(cookingListViewModel);

        final RecipeDataAccessInterface cookingListDao =
                new RecipeDataAccessObject();

        final AddToCookingListInputBoundary addToCookingListInteractor =
                new AddToCookingListInteractor(cookingListDao, cookingListPresenter);

        final AddToCookingListController addToCookingListController =
                new AddToCookingListController(addToCookingListInteractor);

        final SortCookingListOutputBoundary sortCookingListPresenter =
                new SortCookingListPresenter(cookingListViewModel);

        final SortCookingListInputBoundary sortCookingListInteractor =
                new SortCookingListInteractor(cookingListDao, sortCookingListPresenter);

        final SortCookingListController sortCookingListController =
                new SortCookingListController(sortCookingListInteractor);

        final CookingListView cookingListView =
                new CookingListView(cookingListViewModel, sortCookingListController);

        // --- Favorite list wiring ---
        final FavoriteListViewModel favoriteListViewModel =
                new FavoriteListViewModel();

        final AddFavoriteRecipeOutputBoundary favoritePresenter =
                new AddFavoriteRecipePresenter(favoriteListViewModel);

        final AddFavoriteRecipeDataAccessInterface favoriteDao =
                new InMemoryFavoriteRecipeDataAccess();

        final AddFavoriteRecipeInputBoundary addFavoriteRecipeInteractor =
                new AddFavoriteRecipeInteractor(favoriteDao, favoritePresenter);

        final AddFavoriteRecipeController addFavoriteRecipeController =
                new AddFavoriteRecipeController(addFavoriteRecipeInteractor);

        final FavoriteListView favoriteListView =
                new FavoriteListView(favoriteListViewModel);

        favoriteListView.setFavoriteController(addFavoriteRecipeController);

        // --- Fridge wiring (Add / Get / Delete) ---
        final FridgeViewModel fridgeViewModel = new FridgeViewModel();
        final FridgePresenter fridgePresenter = new FridgePresenter(fridgeViewModel);

        final AddToFridgeInputBoundary addToFridgeInteractor =
                new AddToFridgeInteractor(ingredientDataAccess, fridgePresenter);

        final GetFridgeInputBoundary getFridgeInteractor =
                new GetFridgeInteractor(ingredientDataAccess, fridgePresenter);

        final DeleteFridgeInputBoundary deleteFridgeInteractor =
                new DeleteFridgeInteractor(ingredientDataAccess, fridgePresenter);

        final FridgeController fridgeController =
                new FridgeController(
                        addToFridgeInteractor,
                        getFridgeInteractor,
                        deleteFridgeInteractor,
                        loginViewModel
                );

        final FridgeView fridgeView = new FridgeView(fridgeViewModel);
        fridgeView.setController(fridgeController);

        // ★ 当在 Ingredient 页面点击 “Add” 时，同步加入 fridge
        ingredientSearchView.setOnAddToFridge(name -> {
            try {
                final Ingredient ing = Ingredient.builder()
                        .setName(name)
                        .setId(-1)
                        .build();
                fridgeController.addIngredient(ing);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // --- Recipe Instruction View ---
        final RecipeInstructionView recipeInstructionView =
                new RecipeInstructionView();
        recipeInstructionView.setFavoriteController(addFavoriteRecipeController);

        // 让 recipe 搜索页能把菜加到 favorites 和 cooking list
        recipeSearchView.setFavoriteController(addFavoriteRecipeController);
        recipeSearchView.setCookingListController(addToCookingListController);

        recipeSearchController.setFavoriteController(addFavoriteRecipeController);

        // 让 cooking list 能打开 recipe details
        cookingListView.setOnOpenRecipe(recipeSearchController::openRecipe);

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
        final String cooking = "cooking";
        final String favorites = "favorites";
        final String recipeInstruction = "recipeInstruction";
        final String fridge = "fridge";
        final String recipeTitle = "What2Cook - Recipes";
        final String ingredientTitle = "What2Cook - Ingredients";
        favoriteListView.setOnBackToRecipes(() -> {
            frame.setTitle(recipeTitle);
            cardLayout.show(cardPanel, recipe);
        });

        // Fridge 页面的 Back：回到 Ingredients 页面
        fridgeView.setOnBack(() -> {
            frame.setTitle(ingredientTitle);
            cardLayout.show(cardPanel, ingredient);
        });

        cardPanel.add(loginView, login);
        cardPanel.add(signupView, signup);
        cardPanel.add(ingredientSearchView, ingredient);
        cardPanel.add(recipeSearchView, recipe);
        cardPanel.add(cookingListView, cooking);
        cardPanel.add(favoriteListView, favorites);
        cardPanel.add(recipeInstructionView, recipeInstruction);
        cardPanel.add(fridgeView, fridge);

        // Instruction 页面的返回按钮：回到 recipes
        recipeInstructionView.setOnBackToRecipeList(() -> {
            frame.setTitle(recipeTitle);
            cardLayout.show(cardPanel, recipe);
        });

        // Cooking list 的返回按钮：回到 recipes
        cookingListView.setOnBack(() -> {
            frame.setTitle(recipeTitle);
            cardLayout.show(cardPanel, recipe);
        });

        // --- Navigation wiring ---
        loginView.setOnSwitchToSignup(() -> {
            frame.setTitle("What2Cook - Sign Up");
            cardLayout.show(cardPanel, signup);
        });

        signupView.setOnBackToLogin(() -> {
            frame.setTitle("What2Cook - Login");
            cardLayout.show(cardPanel, login);
        });

        // 从 recipe 列表打开 instruction 页
        recipeSearchView.setOnOpenInstruction(recipeObj -> {
            recipeInstructionView.setRecipe(recipeObj);
            frame.setTitle("What2Cook - Instructions");
            cardLayout.show(cardPanel, recipeInstruction);
        });

        // 登录成功：进入 Ingredient 页面
        loginView.setOnLoginSuccess(() -> {
            final String username = loginViewModel.getState().getUsername();
            recipeSearchView.setCurrentUsername(username);
            recipeInstructionView.setCurrentUsername(username);
            recipeSearchController.setCurrentUsername(username);
            favoriteListView.setCurrentUsername(username);
            cookingListView.setCurrentUsername(username);
            frame.setTitle(ingredientTitle);
            cardLayout.show(cardPanel, ingredient);
        });

        // --- Ingredient → Recipe flow ---
        ingredientSearchView.setOnNext(() -> {
            final List<String> names =
                    ingredientSearchViewModel.getState().getIngredients();

            final List<Ingredient> ingredientsList = new ArrayList<>();
            for (String name : names) {
                ingredientsList.add(
                        Ingredient.builder()
                                .setName(name)
                                .setId(-1)
                                .build()
                );
            }

            recipeSearchViewModel.setCurrentIngredients(ingredientsList);
            recipeSearchController.searchByIngredients(ingredientsList);

            frame.setTitle(recipeTitle);
            cardLayout.show(cardPanel, recipe);
        });

        // 从 Ingredient 页面打开 Fridge 页面
        ingredientSearchView.setOnOpenFridge(() -> {
            try {
                fridgeController.GetIngredient();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            frame.setTitle("What2Cook - Fridge");
            cardLayout.show(cardPanel, fridge);
        });

        // 从 recipe 页 “Back” 回到 Ingredient 页
        recipeSearchView.setOnBack(() -> {
            frame.setTitle(ingredientTitle);
            cardLayout.show(cardPanel, ingredient);
        });

        // 从 recipes 打开 cooking list
        recipeSearchView.setOnOpenCookingList(() -> {
            final String username = loginViewModel.getState().getUsername();
            cookingListView.setCurrentUsername(username);

            frame.setTitle("What2Cook - Cooking List");
            cardLayout.show(cardPanel, cooking);
        });

        // 从 recipes 打开 favorites
        recipeSearchView.setOnOpenFavorites(() -> {
            frame.setTitle("What2Cook - Favorites");
            cardLayout.show(cardPanel, favorites);
        });

        frame.add(cardPanel);
        frame.setTitle("What2Cook - Login");
        cardLayout.show(cardPanel, login);

        return frame;
    }
}
