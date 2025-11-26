package recipeapi.exceptions;

public class IngredientNotFoundException extends Exception {
    public IngredientNotFoundException(String ingredient) {
        super("Ingredient not found: " + ingredient);
    }
}
