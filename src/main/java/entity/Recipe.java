package entity;

import java.util.List;

public class Recipe {
    private int id;
    private String title;
    private List<String> ingredientNames;
    private double calories;
    private int healthScore;
    private String instructions;
    private String image;

    public Recipe() {
    }

    public Recipe(int id, String title,
                  List<String> ingredientNames,
                  double calories,
                  int healthScore,
                  String instructions) {
        this.id = id;
        this.title = title;
        this.ingredientNames = ingredientNames;
        this.calories = calories;
        this.healthScore = healthScore;
        this.instructions = instructions;
        this.image = "";
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

    public void setImage(String image) {
        this.image = image;
    }
}
