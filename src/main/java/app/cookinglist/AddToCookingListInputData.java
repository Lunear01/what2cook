package app.cookinglist;

public interface AddToCookingListInputData {
    private final String username;
    private final Recipe recipe;

    public AddToCookingListInputData(String username, Recipe recipe) {
        this.username = username;
        this.recipe = recipe;
    }

    public String getUsername() {
        return username;
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
