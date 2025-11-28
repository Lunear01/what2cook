package dataaccess;

import entity.Ingredient;

public interface IngredientDataAccess {
    /**
     *
     * @param id
     * @return
     */
    Ingredient getIngredient(int id);
}
