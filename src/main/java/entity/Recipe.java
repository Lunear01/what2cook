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

    public static RecipeBuilder builder() {
        return new RecipeBuilder();
    }
    }
