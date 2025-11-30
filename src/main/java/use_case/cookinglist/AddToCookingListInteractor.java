package use_case.cookinglist;

import entity.Recipe;

import java.util.ArrayList;
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

        // Bug #4 修复: 移除过于宽泛的异常处理，因为 DAO 层现在正确处理 404
        final List<Recipe> currentList = cookingListDao.getAllRecipes(username);

        // 判断是否已经存在
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

        // 添加 Recipe 对象
        cookingListDao.addRecipe(username, recipe);

        // 获取更新后的列表
        final List<Recipe> updatedList = cookingListDao.getAllRecipes(username);

        presenter.present(
                new AddToCookingListOutputData(
                        updatedList,
                        recipe.getTitle() + " added to your cooking list!"
                )
        );
    }
}
