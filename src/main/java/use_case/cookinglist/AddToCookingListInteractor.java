package use_case.cookinglist;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class AddToCookingListInteractor implements AddToCookingListInputBoundary {
//make sure this is using recipe data access interface instead of cooking list interface
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

        final List<Recipe> currentList = cookingListDao.getAllRecipes(username);

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

        cookingListDao.addRecipe(username, recipe);

        final List<Recipe> updatedList = cookingListDao.getAllRecipes(username);
        presenter.present(
                new AddToCookingListOutputData(
                        updatedList,
                        recipe.getTitle() + " added to your cooking list!"
                )
        );
    }
}
