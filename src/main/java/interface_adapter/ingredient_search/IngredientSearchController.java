package interface_adapter.ingredient_search;

import use_case.ingredient_search.IngredientSearchInputBoundary;
import use_case.ingredient_search.IngredientSearchInputData;

public class IngredientSearchController {

    private final IngredientSearchInputBoundary interactor;

    /** 当前登录用户的用户名，由 RecipeAppBuilder 在登录成功后注入 */
    private String currentUserName;

    public IngredientSearchController(IngredientSearchInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * 在用户登录成功之后，由外部（例如 RecipeAppBuilder）调用，
     * 把当前用户名存起来，后面每次添加食材都会带上这个用户名。
     */
    public void setCurrentUserName(String userName) {
        this.currentUserName = userName;
    }

    /**
     * View 调用：用户在输入框里点 Add 时调用。
     */
    public void addIngredient(String ingredientName) {
        if (currentUserName == null || currentUserName.isEmpty()) {
            throw new IllegalStateException("Current user name is not set for IngredientSearchController");
        }
        final IngredientSearchInputData input =
                new IngredientSearchInputData(currentUserName, ingredientName);
        interactor.execute(input);
    }

    /**
     * 如果你有“刚打开页面时就加载已有食材”的需求，可以再加一个：
     */
    public void loadIngredients() {
        if (currentUserName == null || currentUserName.isEmpty()) {
            throw new IllegalStateException("Current user name is not set for IngredientSearchController");
        }
        final IngredientSearchInputData input =
                new IngredientSearchInputData(currentUserName, "");
        interactor.execute(input);
    }
}
