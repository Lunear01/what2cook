package entity;

import java.util.List;

public class Recipe {
    private final String title;
    private final List<String> ingredientNames;
    private final int calories;
    private final int healthScore;
    private final String instructions;

    private Recipe(RecipeBuilder builder) {
        this.title = builder.title;
        this.ingredientNames = builder.ingredientNames;
        this.calories = builder.calories;
        this.healthScore = builder.healthScore;
        this.instructions = builder.instructions;
    }

    public static class RecipeBuilder {
        private String title;
        private List<String> ingredientNames;
        private int calories;
        private int healthScore;
        private String instructions;

        public RecipeBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public RecipeBuilder setIngredientNames(List<String> ingredientNames) {
            this.ingredientNames = ingredientNames;
            return this;
        }

        public RecipeBuilder setCalories(int calories) {
            this.calories = calories;
            return this;
        }

        public RecipeBuilder setHealthScore(int healthScore) {
            this.healthScore = healthScore;
            return this;
        }

        public RecipeBuilder setInstructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}