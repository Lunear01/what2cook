package entity;

import java.util.List;

public class Recipe {
    private final String title;
    private final List<String> ingredientNames;
    private final int calories;
    private final int healthScore;
    private final String instructions;

    public Recipe(String title,
                  List<String> ingredientNames,
                  int calories,
                  int healthScore,
                  String instructions) {
        this.title = title;
        this.ingredientNames = ingredientNames;
        this.calories = calories;
        this.healthScore = healthScore;
        this.instructions = instructions;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public int getCalories() {
        return calories;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public String getInstructions() {
        return instructions;
    }
}

