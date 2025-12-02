package use_case.recipe_search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.Ingredient;
import entity.Recipe;
import recipeapi.RecipeFetcher;
import recipeapi.exceptions.IngredientNotFoundException;
import recipeapi.exceptions.RecipeNotFoundException;

/**
 * Interactor for the recipe search use case.
 * Its responsibilities:
 * 1. Validate user input (list of ingredients).
 * 2. Call RecipeFetcher to retrieve recipe results.
 * 3. Enrich each recipe with detailed info and instructions.
 * 4. Handle domain and IO exceptions.
 * 5. Pass results to the output boundary (presenter).
 */
public class RecipeSearchInteractor implements RecipeSearchInputBoundary {

    private static final int DEFAULT_NUMBER = 10;
    private static final int DEFAULT_RANKING = 1;
    private static final boolean DEFAULT_IGNORE_PANTRY = true;

    private final RecipeFetcher fetcher;
    private final RecipeSearchOutputBoundary presenter;

    public RecipeSearchInteractor(RecipeFetcher fetcher,
                                  RecipeSearchOutputBoundary presenter) {
        this.fetcher = fetcher;
        this.presenter = presenter;
    }

    /**
     * Executes the recipe search flow using the given input data.
     * Steps:
     * 1. Ensure ingredient list is non-empty.
     * 2. Convert Ingredient objects to names.
     * 3. Fetch initial recipe results.
     * 4. For each recipe, fetch detailed info and instructions.
     * 5. Build enriched recipes.
     * 6. Pass the result to the presenter (success or fail).
     */
    @Override
    public void execute(RecipeSearchInputData inputData) {
        final List<Ingredient> ingredients = inputData.getIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            presenter.prepareFailView(new RecipeSearchOutputData(
                    new ArrayList<>(),
                    new ArrayList<>(),
                    "Please add at least one ingredient before searching."
            ));
        }
        else {
            try {
                final List<String> names = new ArrayList<>();
                for (Ingredient ingredient : ingredients) {
                    names.add(ingredient.getName());
                }

                final List<Recipe> basic = fetcher.getRecipesByIngredients(
                        names, DEFAULT_NUMBER, DEFAULT_RANKING, DEFAULT_IGNORE_PANTRY
                );

                final List<Recipe> enriched = new ArrayList<>();

                for (Recipe recipe : basic) {
                    final int id = recipe.getId();

                    final Recipe info = fetcher.getRecipeInfo(id, true, false, false);

                    final Recipe instructions = fetcher.getRecipeInstructions(id, true);

                    final Recipe updated = recipe.toBuilder()
                            .setHealthScore(info.getHealthScore())
                            .setIngredientNames(info.getIngredients())
                            .setCalories(info.getCalories())
                            .setInstructions(instructions.getInstructions())
                            .build();

                    enriched.add(updated);
                }

                presenter.prepareSuccessView(new RecipeSearchOutputData(
                        new ArrayList<>(ingredients),
                        enriched,
                        null
                ));
            }
            catch (IngredientNotFoundException | RecipeNotFoundException domainException) {
                presenter.prepareFailView(new RecipeSearchOutputData(
                        new ArrayList<>(ingredients),
                        new ArrayList<>(),
                        domainException.getMessage()
                ));
            }

            catch (IOException ioException) {
                presenter.prepareFailView(new RecipeSearchOutputData(
                        new ArrayList<>(ingredients),
                        new ArrayList<>(),
                        "Network error while fetching recipes: " + ioException.getMessage()
                ));
            }
        }
    }
}
