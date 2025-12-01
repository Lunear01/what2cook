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

    /**
     * Sets the recipe ID.
     *
     * @param recipeID the unique ID of the recipe
     * @return this builder instance
     */
    public RecipeBuilder setId(int recipeID) {
        this.recipeID = recipeID;
        return this;
    }

    /**
     * Sets the recipe title.
     *
     * @param title the title of the recipe
     * @return this builder instance
     */
    public RecipeBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the list of ingredients for the recipe.
     *
     * @param ingredientNames the list of ingredient objects
     * @return this builder instance
     */
    public RecipeBuilder setIngredientNames(List<Ingredient> ingredientNames) {
        this.ingredientNames = ingredientNames;
        return this;
    }

    /**
     * Sets the calorie count for the recipe.
     *
     * @param calories the caloric value of the recipe
     * @return this builder instance
     */
    public RecipeBuilder setCalories(double calories) {
        this.calories = calories;
        return this;
    }

    /**
     * Sets the health score of the recipe.
     *
     * @param healthScore the health score assigned to the recipe
     * @return this builder instance
     */
    public RecipeBuilder setHealthScore(int healthScore) {
        this.healthScore = healthScore;
        return this;
    }

    /**
     * Sets the instructions for preparing the recipe.
     *
     * @param instructions the cooking instructions
     * @return this builder instance
     */
    public RecipeBuilder setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    /**
     * Sets the recipe image URL or file name.
     *
     * @param image the image associated with the recipe
     * @return this builder instance
     */
    public RecipeBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    /**
     * Builds a new {@link Recipe} object using the current builder values.
     *
     * @return the constructed Recipe instance
     */
    public Recipe build() {
        return new Recipe(recipeID, title, ingredientNames, calories, healthScore, instructions, image);
    }
}

