package use_case.cookinglist;

import entity.Recipe;

import java.util.List;

public class AddToCookingListInteractor implements AddToCookingListInputBoundary {

    private final CookingListDataAccessInterface cookingListDao;
    private final AddToCookingListOutputBoundary presenter;

    public AddToCookingListInteractor(CookingListDataAccessInterface cookingListDao,
                                      AddToCookingListOutputBoundary presenter) {
        this.cookingListDao = cookingListDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddToCookingListInputData inputData) {

        final String username = inputData.getUsername();
        final Recipe recipe = inputData.getRecipe();

        // 1. 当前列表
        final List<Recipe> currentList = cookingListDao.getCookingList(username);

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

        cookingListDao.addToCookingList(username, recipe);

        final List<Recipe> updatedList = cookingListDao.getCookingList(username);

        presenter.present(
                new AddToCookingListOutputData(
                        updatedList,
                        recipe.getTitle() + " added to your cooking list!"
                )
        );
    }
}
