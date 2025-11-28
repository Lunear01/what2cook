package entity;

public class Ingredient {

    private final String name;
    private final int ingredientId;

    private Ingredient(Builder builder) {
        this.name = builder.name;
        this.ingredientId = builder.ingredientId;
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
