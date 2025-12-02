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
        System.out.println("\n--- Interactor: execute() started ---");
        try {
            final String username = inputData.getUsername();
            final Recipe recipe = inputData.getRecipe();

            System.out.println("DEBUG Interactor: Input data received");
            System.out.println("  - Username: " + username);
            System.out.println("  - Recipe ID: " + recipe.getId());
            System.out.println("  - Recipe Title: " + recipe.getTitle());

            System.out.println("DEBUG Interactor: Fetching current cooking list from DAO...");
            final List<Recipe> currentList = cookingListDao.getAllRecipes(username);
            System.out.println("DEBUG Interactor: Current list size = " + currentList.size());

            final boolean exists = currentList.stream()
                    .anyMatch(recipeItem -> recipeItem.getId() == recipe.getId());

            System.out.println("DEBUG Interactor: Recipe exists in list = " + exists);

            if (exists) {
                System.out.println("DEBUG Interactor: Recipe already exists, notifying presenter");
                presenter.present(
                        new AddToCookingListOutputData(
                                currentList,
                                recipe.getTitle() + " is already in your cooking list."
                        )
                );
                System.out.println("DEBUG Interactor: Presenter notified (duplicate)");
            }
            else {
                System.out.println("DEBUG Interactor: Adding new recipe to DAO...");
                cookingListDao.addRecipe(username, recipe);
                System.out.println("DEBUG Interactor: DAO.addRecipe() completed successfully");

                System.out.println("DEBUG Interactor: Fetching updated list from DAO...");
                final List<Recipe> updatedList = cookingListDao.getAllRecipes(username);
                System.out.println("DEBUG Interactor: Updated list size = " + updatedList.size());

                System.out.println("DEBUG Interactor: Notifying presenter with success message");
                presenter.present(
                        new AddToCookingListOutputData(
                                updatedList,
                                recipe.getTitle() + " added to your cooking list!"
                        )
                );
                System.out.println("DEBUG Interactor: Presenter notified (success)");
            }
        }
        catch (Exception e) {
            System.err.println("ERROR Interactor: Exception occurred!");
            System.err.println("  Exception type: " + e.getClass().getName());
            System.err.println("  Message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("--- Interactor: execute() completed ---\n");
    }
}
