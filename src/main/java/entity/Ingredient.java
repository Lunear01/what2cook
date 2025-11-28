package entity;

public class Ingredient {

    private final String name;
    private final int ingredientId;

    Ingredient(String name, int ingredientId) {
        this.name = name;
        this.ingredientId = ingredientId;
    }

    /**
     * Returns a new builder instance for creating an Ingredient.
     *
     * @return a new {@code IngredientBuilder}.
     */
    public static IngredientBuilder builder() {
        return new IngredientBuilder();
    }

    public String getName() {
        return name;
    }

    public int getIngredientId() {
        return ingredientId;
    }


}
