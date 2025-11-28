package entity;

import java.util.Collections;
import java.util.List;

public class RecipeBuilder {
    private int recipeID;
    private String title = "";
    private List<Ingredient> ingredientNames = Collections.emptyList();
    private double calories;
    private int healthScore;
    private String instructions = "";
    private String image = "";

    public RecipeBuilder setId(int recipeID) {
        this.recipeID = recipeID;
        return this;
    }

    public RecipeBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public RecipeBuilder setIngredientNames(List<Ingredient> ingredientNames) {
        this.ingredientNames = ingredientNames;
        return this;
    }

    public RecipeBuilder setCalories(double calories) {
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

    public RecipeBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public Recipe build() {
        return new Recipe(recipeID, title, ingredientNames, calories, healthScore, instructions, image);
    }
}

