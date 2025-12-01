package use_case.cookinglist.GetCookingList;

import java.util.List;

import entity.Recipe;
import use_case.cookinglist.RecipeDataAccessInterface;

/**
 * Interactor for getting the cooking list.
 */
public class GetCookingListInteractor implements GetCookingListInputBoundary {
    private final RecipeDataAccessInterface cookingListDao;
    private final GetCookingListOutputBoundary presenter;

    public GetCookingListInteractor(RecipeDataAccessInterface cookingListDao,
                                    GetCookingListOutputBoundary presenter) {
        this.cookingListDao = cookingListDao;
        this.presenter = presenter;
    }

    @Override
    public void execute(GetCookingListInputData inputData) {
        final String username = inputData.getUsername();

        try {
            final List<Recipe> cookingList = cookingListDao.getAllRecipes(username);

            final String message = cookingList.isEmpty()
                ? "Your cooking list is empty."
                : "Loaded " + cookingList.size() + " recipe(s).";

            presenter.present(new GetCookingListOutputData(cookingList, message));
        }
        catch (RuntimeException exception) {
            presenter.present(new GetCookingListOutputData(
                List.of(),
                "Your cooking list is empty."
            ));
        }
    }
}
