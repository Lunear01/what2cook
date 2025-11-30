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

        // 1. 当前列表（如果用户是第一次使用，可能返回404，使用空列表）
        List<Recipe> currentList;
        try {
            currentList = cookingListDao.getAllRecipes(username);
        } catch (RuntimeException e) {
            // 用户第一次使用，还没有 cooking list，使用空列表
            currentList = new ArrayList<>();
        }

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

        // 直接添加 Recipe 对象
        cookingListDao.addRecipe(username, recipe);

        // 获取更新后的列表
        List<Recipe> updatedList;
        try {
            updatedList = cookingListDao.getAllRecipes(username);
        } catch (RuntimeException e) {
            // 如果获取失败，至少返回当前添加的 recipe
            updatedList = new ArrayList<>(currentList);
            updatedList.add(recipe);
        }

        presenter.present(
                new AddToCookingListOutputData(
                        updatedList,
                        recipe.getTitle() + " added to your cooking list!"
                )
        );
    }
}
