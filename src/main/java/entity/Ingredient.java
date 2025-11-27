package entity;

public class Ingredient {
    private String name;
    private int ingredientId;

    public Ingredient(String name, int ingredientId) {
        this.name = name;
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

}
