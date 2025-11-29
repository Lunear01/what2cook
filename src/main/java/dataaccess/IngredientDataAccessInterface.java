package dataaccess;

import entity.Ingredient;

import java.util.List;

public interface IngredientDataAccessInterface {
    /**
     * add the ingredient.
     * @param userName the name of current user
     * @param ingredientID the id of the ingredient that need to be added
     * @param ingredientName the name of the ingredient that need to be added
     */
    void addIngredient(String userName, int ingredientID, String ingredientName);

    /**
     * get the ingredient.
     * @param userName the name of the current user
     * @return the list of the ingredients that was created by user.
     */
    List<Ingredient> getAllIngredients(String userName);

    /**
     * delete the ingredient.
     * @param userName the name of current user
     * @param ingredientID the id of the ingredient that need to be deleted
     */
    void deleteIngredient(String userName, int ingredientID);

}
