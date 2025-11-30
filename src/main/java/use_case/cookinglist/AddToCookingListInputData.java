package use_case.cookinglist;

import org.w3c.dom.CDATASection;

import entity.Recipe;

public class AddToCookingListInputData {
    private final String username;
    private final Recipe recipe;

    public AddToCookingListInputData(String username, Recipe recipe) {
        // Bug #15 修复: 添加参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
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
