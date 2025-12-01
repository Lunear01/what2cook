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

    /**
     * Returns a new {@code IngredientBuilder} initialized with the fields of this
     * {@code Ingredient}.
     * This method allows safe modification of an existing {@code Ingredient}
     * instance by creating a new builder pre-filled with its current values.
     * @return a new {@code IngredientBuilder} pre-populated with this object's data
     */
    public IngredientBuilder toBuilder() {
        return new IngredientBuilder()
                .setName(this.name)
                .setId(this.ingredientId);
    }
}
