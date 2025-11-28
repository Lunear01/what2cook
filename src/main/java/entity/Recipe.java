package entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private final int recipeID;
    private final String title;
    private final List<Ingredient> ingredientNames;
    private final double calories;
    private final int healthScore;
    private final String instructions;
    private final String image;

    private Recipe(Builder builder) {
        this.recipeID = builder.recipeID;
        this.title = builder.title;
        this.ingredientNames = builder.ingredientNames;
        this.calories = builder.calories;
        this.healthScore = builder.healthScore;
        this.instructions = builder.instructions;
        this.image = builder.image;
    }

    public int getId() {
        return recipeID;
    }

    public String getTitle() {
        return title;
    }

    public List<Ingredient> getIngredientNames() {
        return Objects.requireNonNullElse(ingredientNames, Collections.emptyList());
    }

    public double getCalories() {
        return calories;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImage() {
        return image;
    }

    public Builder toBuilder() {
        return new Builder()
                .setId(this.recipeID)
                .setTitle(this.title)
                .setIngredientNames(this.ingredientNames)
                .setCalories(this.calories)
                .setHealthScore(this.healthScore)
                .setInstructions(this.instructions)
                .setImage(this.image);
    }

    public static class Builder {
        private int recipeID;
        private String title = "";
        private List<Ingredient> ingredientNames = Collections.emptyList();
        private double calories;
        private int healthScore;
        private String instructions = "";
        private String image = "";

        public Builder setId(int recipeID) {
            this.recipeID = recipeID;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setIngredientNames(List<Ingredient> ingredientNames) {
            this.ingredientNames = ingredientNames;
            return this;
        }

        public Builder setCalories(double calories) {
            this.calories = calories;
            return this;
        }

        public Builder setHealthScore(int healthScore) {
            this.healthScore = healthScore;
            return this;
        }

        public Builder setInstructions(String instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }
}
