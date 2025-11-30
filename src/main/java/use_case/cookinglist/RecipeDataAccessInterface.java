package use_case.cookinglist;

import entity.Recipe;
import org.json.JSONObject;

import java.util.List;

public interface RecipeDataAccessInterface {
    /**
     * add the recipe.
     * @param userName the name of the current user
     * @param recipeID the id of the recipe that needs to be added
     * @param recipes the name of the recipe that needs to be added
     */
    void addRecipe(String userName, int recipeID, JSONObject recipes);

    /**
     * get the recipe.
     * @param userName the name of the current user
     * @return the list of the recipes that was created by user.
     */
    List<Recipe> getAllRecipes(String userName);

    /**
     * delete the recipe.
     * @param userName the name of the current user
     * @param recipeID the id of the recipe that need to be deleted
     */
    void deleteRecipe(String userName, int recipeID);

}
