package recipeapi;

import java.util.List;

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

    Recipe getRecipeInfo(int id, boolean includeNutrition, boolean addWinePairing,
                         boolean addTasteData) throws RecipeNotFoundException;

    Recipe getRecipeInstructions(int id, boolean steBreakdown) throws RecipeNotFoundException;

    Recipe getNutrition(int id) throws RecipeNotFoundException;

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
