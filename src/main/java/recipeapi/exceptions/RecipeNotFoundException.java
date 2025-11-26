package recipeapi.exceptions;

public class RecipeNotFoundException extends Exception {
    public RecipeNotFoundException(String recipe) {
        super("Recipe not found: " + recipe);
    }
}
