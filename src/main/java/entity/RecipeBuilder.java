package entity;

public class RecipeBuilder {
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

