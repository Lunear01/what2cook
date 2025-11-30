package use_case.fridge;

import entity.Ingredient;

import java.util.List;

public interface IngredientDataAccessInterface {
    /**
     * add the ingredient.
     * @param userName the name of current user
     * @param ingredientName the name of the ingredient that need to be added
     */
    void addIngredient(String userName, String ingredientName);

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

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @param ingredientID the id of the ingredient to look for
     * @return true if an ingredient with the given username and idexists; false otherwise
     */
    boolean exists(String username, int ingredientID);

}
