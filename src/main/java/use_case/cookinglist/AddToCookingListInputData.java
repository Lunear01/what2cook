package use_case.cookinglist;

import org.w3c.dom.CDATASection;
import entity.Recipe;

public class AddToCookingListInputData {
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
