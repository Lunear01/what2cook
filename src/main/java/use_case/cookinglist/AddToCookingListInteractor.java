package use_case.cookinglist;

import entity.Ingredient;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class AddToCookingListInteractor implements AddToCookingListInputBoundary {

    private final RecipeDataAccessInterface cookingListDao;
    private final AddToCookingListOutputBoundary presenter;

    public AddToCookingListInteractor(RecipeDataAccessInterface cookingListDao,
                                      AddToCookingListOutputBoundary presenter) {
        this.cookingListDao = cookingListDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddToCookingListInputData inputData) {

        final String username = inputData.getUsername();
        final Recipe recipe = inputData.getRecipe();

        // 1. 当前列表
        final List<Recipe> currentList = cookingListDao.getAllRecipes(username);

        // 2. 判断是否已经存在
        final boolean exists = currentList.stream()
                .anyMatch(r -> r.getId() == recipe.getId());

        if (exists) {
            presenter.present(
                    new AddToCookingListOutputData(
                            currentList,
                            recipe.getTitle() + " is already in your cooking list."
                    )
            );
            return;
        }

        // 将 Recipe 转换为 JSONObject 并添加
        final JSONObject recipeJSON = recipeToJSON(recipe);
        cookingListDao.addRecipe(username, recipe.getId(), recipeJSON);

        final List<Recipe> updatedList = cookingListDao.getAllRecipes(username);

        presenter.present(
                new AddToCookingListOutputData(
                        updatedList,
                        recipe.getTitle() + " added to your cooking list!"
                )
        );
    }

    /**
     * 将 Recipe 对象转换为 JSONObject
     */
    private JSONObject recipeToJSON(Recipe recipe) {
        final JSONObject json = new JSONObject();
        json.put("recipeID", recipe.getId());
        json.put("title", recipe.getTitle());
        json.put("calories", recipe.getCalories());
        json.put("healthScore", recipe.getHealthScore());
        json.put("instructions", recipe.getInstructions());
        json.put("image", recipe.getImage());

        // 转换 ingredients
        final JSONArray ingredientsArray = new JSONArray();
        for (Ingredient ingredient : recipe.getIngredients()) {
            final JSONObject ingredientJSON = new JSONObject();
            ingredientJSON.put("ingredient_id", ingredient.getIngredientId());
            ingredientJSON.put("ingredient_name", ingredient.getName());
            ingredientsArray.put(ingredientJSON);
        }
        json.put("ingredientNames", ingredientsArray);

        return json;
    }
}
