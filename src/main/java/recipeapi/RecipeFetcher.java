package recipeapi;

import java.io.IOException;
import java.util.List;

import entity.Recipe;
import recipeapi.exceptions.IngredientNotFoundException;
import recipeapi.exceptions.RecipeNotFoundException;

/**
 * Interface for the service of getting relevant recipe information.
 */
public interface RecipeFetcher {

    /**
     * Fetch the list of recipes by ingredients.
     * @param ingredients comma separated list of ingredients as strings
     * @param number maximum number of recipes to return
     * @param ranking int, whether to maximize used ingredients (1) or minimize missing ingredients (2) first.
     * @param ignorePantry boolean, whether to ignore typical items such as water.
     * @return list of recipes
     * @throws IngredientNotFoundException if the ingredient does not exist
     * @throws IOException for other errors.
     */
    List<Recipe> getRecipesByIngredients(List<String> ingredients, int number, int ranking,
                                         boolean ignorePantry) throws IngredientNotFoundException, IOException;

    /**
     * Gets detailed information for a specific recipe.
     * @param id the recipe ID
     * @param includeNutrition true to include nutrition data
     * @param addWinePairing true to include wine pairing suggestions
     * @param addTasteData true to include taste analysis
     * @return detailed recipe information
     * @throws RecipeNotFoundException if recipe ID is not found
     * @throws IOException for other errors.
     */
    Recipe getRecipeInfo(int id, boolean includeNutrition, boolean addWinePairing,
                         boolean addTasteData) throws RecipeNotFoundException, IOException;

    /**
     * Gets cooking instructions for a recipe.
     * @param id the recipe ID
     * @param stepBreakdown true to include detailed step-by-step instructions
     * @return recipe with instructions
     * @throws RecipeNotFoundException if recipe ID is not found
     * @throws IOException for other errors.
     */
    Recipe getRecipeInstructions(int id, boolean stepBreakdown) throws RecipeNotFoundException, IOException;

}
