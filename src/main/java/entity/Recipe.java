package entity;

import java.util.List;

public class Recipe {
    private int id;
    private String title;
    private List<String> ingredientNames;
    private int calories;
    private int healthScore;
    private String instructions;

    public Recipe() {
    }

    public Recipe(int id, String title,
                  List<String> ingredientNames,
                  int calories,
                  int healthScore,
                  String instructions) {
        this.id = id;
        this.title = title;
        this.ingredientNames = ingredientNames;
        this.calories = calories;
        this.healthScore = healthScore;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIngredientNames(List<String> ingredientNames) {

        this.ingredientNames = ingredientNames;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setHealthScore(int healthScore) {
        this.healthScore = healthScore;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}

