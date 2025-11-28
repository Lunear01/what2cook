package entity;

public class Ingredient {

    private final String name;
    private final int ingredientId;

    private Ingredient(Builder builder) {
        this.name = builder.name;
        this.ingredientId = builder.ingredientId;
    }

    public String getName() {
        return name;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public Builder toBuilder() {
        return new Builder()
                .setName(this.name)
                .setIngredientId(this.ingredientId);
    }

    public static class Builder {
        private String name;
        private int ingredientId;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setIngredientId(int ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public Ingredient build() {
            return new Ingredient(this);
        }
    }
}
