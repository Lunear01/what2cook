package use_case.recipe_search;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import entity.Recipe;
import recipeapi.RecipeFetcher;
import recipeapi.exceptions.IngredientNotFoundException;

/**
 * Interactor for the "search recipes by ingredients" use case.
 * Implements the application-specific business rules.
 */
public class RecipeSearchInteractor implements RecipeSearchInputBoundary {

    private final RecipeFetcher fetcher;
    private final RecipeSearchOutputBoundary presenter;

    /**
     * @param fetcher   the external recipe API / gateway
     * @param presenter the output boundary to present results to the UI layer
     */
    public RecipeSearchInteractor(RecipeFetcher fetcher,
                                  RecipeSearchOutputBoundary presenter) {
        this.fetcher = fetcher;
        this.presenter = presenter;
    }

    @Override
    public void searchByIngredients(RecipeSearchInputData inputData) {
        List<String> ingredientNames = inputData.getIngredientNames();

        // Some sensible defaults for the API call
        final int number = 10;          // how many recipes to request
        final int ranking = 1;          // 1 = maximize used ingredients
        final boolean ignorePantry = true;

        try {
            // 调用外部 API
            List<Recipe> recipes =
                    fetcher.getRecipesByIngredients(
                            ingredientNames, number, ranking, ignorePantry);

            RecipeSearchOutputData output =
                    new RecipeSearchOutputData(recipes, false, null);

            presenter.present(output);
        }
        catch (IngredientNotFoundException e) {
            // Spoonacular 认为这些食材组合找不到任何菜谱
            RecipeSearchOutputData output =
                    new RecipeSearchOutputData(
                            Collections.emptyList(),
                            true,
                            "No recipes found for those ingredients.");
            presenter.present(output);
        }
        catch (IOException e) {
            // 如果你的 RecipeFetcher 接口声明了 IOException，就会走到这里
            RecipeSearchOutputData output =
                    new RecipeSearchOutputData(
                            Collections.emptyList(),
                            true,
                            "Failed to contact recipe service. Please try again.");
            presenter.present(output);
        }
        catch (RuntimeException e) {
            // 目前 SpoonacularRecipeFetcher 在内部把 IOException 包成 RuntimeException
            // 比如 HTTP 402（额度用完）、401（API key 无效）等
            String msg = e.getMessage();
            if (msg == null || msg.isBlank()) {
                msg = "Failed to contact recipe service (unexpected error).";
            } else {
                msg = "Failed to contact recipe service: " + msg;
            }

            RecipeSearchOutputData output =
                    new RecipeSearchOutputData(
                            Collections.emptyList(),
                            true,
                            msg);
            presenter.present(output);
        }
    }
}
