package dataaccess;

import entity.Ingredient;

import java.sql.SQLException;
import java.util.List;

public interface IngredientDataAccess {
    /**
     * add the ingredient.
     * @param user_name the name of current user
     * @param ingredientID the id of the ingredient that need to be added
     * @param ingredient_name the name of the ingredient that need to be added
     */
    void addIngredient(String user_name, int ingredientID, String ingredient_name) throws Exception;

    /**
     * get the ingredient.
     * @param user_name the name of the current user
     * @return the list of the ingredients that was created by user.
     */
    List<Ingredient> getAllIngredients(String user_name) throws Exception;

    /**
     * delete the ingredient.
     * @param user_name the name of current user
     * @param ingredientID the id of the ingredient that need to be deleted
     */
    void deleteIngredient(String user_name, int ingredientID) throws Exception;

}
