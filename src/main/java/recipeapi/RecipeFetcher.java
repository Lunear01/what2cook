package recipeapi;

import java.util.List;

import entity.Ingredient;
import entity.Recipe;

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
     */
    List<Recipe> getRecipesByIngredients(List<String> ingredients, int number, int ranking,
                                         boolean ignorePantry) throws IngredientNotFoundException;

    /**
     * Gets detailed information for a specific recipe.
     * @param id the recipe ID
     * @param includeNutrition true to include nutrition data
     * @param addWinePairing true to include wine pairing suggestions
     * @param addTasteData true to include taste analysis
     * @return detailed recipe information
     * @throws RecipeNotFoundException if recipe ID is not found
     */
    Recipe getRecipeInfo(int id, boolean includeNutrition, boolean addWinePairing,
                         boolean addTasteData) throws RecipeNotFoundException;

    /**
     * Gets cooking instructions for a recipe.
     * @param id the recipe ID
     * @param stepBreakdown true to include detailed step-by-step instructions
     * @return recipe with instructions
     * @throws RecipeNotFoundException if recipe ID is not found
     */
    Recipe getRecipeInstructions(int id, boolean stepBreakdown) throws RecipeNotFoundException;

    /**
     * Gets nutrition information for a recipe.
     * @param id the recipe ID
     * @return recipe with nutrition data
     * @throws RecipeNotFoundException if recipe ID is not found
     */
    Recipe getNutrition(int id) throws RecipeNotFoundException;

    /**
     * Checks whether the Ingredient being inputted is valid.
     * @param name the ingredient's name
     * @return Ingredient
     * @throws IngredientNotFoundException if ingredient is not found
     */
//    Ingredient searchIngredient(String name) throws IngredientNotFoundException;

    class RecipeNotFoundException extends Exception {
        public RecipeNotFoundException(String recipe) {
            super("Recipe not found: " + recipe);
        }
    }

    class IngredientNotFoundException extends Exception {
        public IngredientNotFoundException(String ingredient) {
            super("Ingredient not found: " + ingredient);
        }
    }
}
