package entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private final int recipeID;
    private final String title;
    private final List<Ingredient> ingredientList;
    private final double calories;
    private final int healthScore;
    private final String instructions;
    private final String image;

    Recipe(int recipeID, String title, List<Ingredient> ingredientList, double calories,
           int healthScore, String instructions, String image) {
        this.recipeID = recipeID;
        this.title = title;
        this.ingredientList = ingredientList;
        this.calories = calories;
        this.healthScore = healthScore;
        this.instructions = instructions;
        this.image = image;
    }

    public int getId() {
        return recipeID;
    }

    public String getTitle() {
        return title;
    }

    public List<Ingredient> getIngredients() {
        return Objects.requireNonNullElse(ingredientList, Collections.emptyList());
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

    /**
     * Returns a new RecipeBuilder.
     *
     * @return return new builder for recipe.
     */
    public static RecipeBuilder builder() {
        return new RecipeBuilder();
    }

    /**
     * Converts this Recipe into a RecipeBuilder.
     *
     * @return return new builder for recipe.
     */
    public RecipeBuilder toBuilder() {
        return new RecipeBuilder()
                .setId(this.recipeID)
                .setTitle(this.title)
                .setImage(this.image)
                .setIngredientNames(this.ingredientList)
                .setHealthScore(this.healthScore)
                .setCalories(this.calories)
                .setInstructions(this.instructions);
    }

}
