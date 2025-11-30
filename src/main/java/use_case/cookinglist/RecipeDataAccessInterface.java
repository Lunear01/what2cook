package use_case.cookinglist;

import java.util.List;

import entity.Recipe;

/**
 * Interface for recipe data access operations.
 */
public interface RecipeDataAccessInterface {
    /**
     * Add the recipe.
     * @param userName the name of current user
     * @param recipe the recipe that need to be added
     */
    void addRecipe(String userName, Recipe recipe);

    /**
     * Get the recipe.
     * @param userName the name of the current user
     * @return the list of the recipes that was created by user.
     */
    List<Recipe> getAllRecipes(String userName);

    /**
     * Delete the recipe.
     * @param userName the name of the current user
     * @param recipeID the id of the recipe that need to be deleted
     */
    void deleteRecipe(String userName, int recipeID);
    // make sure this is under use_case/cookinglist
}
