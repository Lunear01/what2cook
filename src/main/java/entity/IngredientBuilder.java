package entity;

public class IngredientBuilder {
    private String name;
    private int ingredientId;

    /**
     * Set the name for this IngredientBuilder.
     *
     * @param name the ingredient's name
     * @return this builder
     */
    public IngredientBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the id for this IngredientBuilder.
     *
     * @param ingredientId the ingredient's id
     * @return this builder
     */
    public IngredientBuilder setId(int ingredientId) {
        this.ingredientId = ingredientId;
        return this;
    }

    public Ingredient build() {

        return new Ingredient(name, ingredientId);

    }
}

