package use_case.cookinglist.AddToCookingList;

import java.util.List;

import entity.Recipe;
import use_case.cookinglist.RecipeDataAccessInterface;

/**
 * Interactor for adding recipes to the cooking list.
 */
public class AddToCookingListInteractor implements AddToCookingListInputBoundary {
    // make sure this is using recipe data access interface instead of cooking list interface
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
        System.out.println("=== 尝试添加菜谱: " + recipe.getTitle() + " (ID: " + recipe.getId() + ") ===");

        final List<Recipe> currentList = cookingListDao.getAllRecipes(username);
        System.out.println("当前 cooking list 有 " + currentList.size() + " 个菜谱:");
        for (Recipe r : currentList) {
            System.out.println("  - " + r.getTitle() + " (ID: " + r.getId() + ")");
        }

        final boolean exists = currentList.stream()
                .anyMatch(recipeItem -> recipeItem.getId() == recipe.getId());

        if (exists) {
            System.out.println(">>> 菜谱已存在，跳过添加");
            presenter.present(
                    new AddToCookingListOutputData(
                            currentList,
                            recipe.getTitle() + " is already in your cooking list."
                    )
            );
        }
        else {
            System.out.println(">>> 调用 addRecipe 添加新菜谱...");
            cookingListDao.addRecipe(username, recipe);
            final List<Recipe> updatedList = cookingListDao.getAllRecipes(username);
            System.out.println("添加后 cooking list 有 " + updatedList.size() + " 个菜谱");
            presenter.present(
                    new AddToCookingListOutputData(
                            updatedList,
                            recipe.getTitle() + " added to your cooking list!"
                    )
            );
        }
    }
}
