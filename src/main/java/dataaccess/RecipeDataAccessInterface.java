package dataaccess;

import entity.Recipe;
import org.json.JSONObject;

import java.util.List;

public interface RecipeDataAccessInterface {
    /**
     * add the recipe.
     * @param user_name the name of current user
     * @param recipeID the id of the recipe that need to be added
     * @param recipes the name of the recipe that need to be added
     */
    void addRecipe(String user_name, int recipeID, JSONObject recipes) throws Exception;

    /**
     * get the recipe.
     * @param user_name the name of the current user
     * @return the list of the recipes that was created by user.
     */
    List<Recipe> getAllRecipes(String user_name) throws Exception;

    /**
     * delete the recipe.
     * @param user_name the name of current user
     * @param recipeID the id of the recipe that need to be deleted
     */
    void deleteRecipe(String user_name, int recipeID) throws Exception;

}
