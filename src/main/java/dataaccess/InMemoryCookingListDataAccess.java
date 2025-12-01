package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Recipe;
import use_case.cookinglist.RecipeDataAccessInterface;

/**
 * In-memory implementation of the cooking list data access.
 * Stores cooking list per user in a simple map.
 */
public class InMemoryCookingListDataAccess implements RecipeDataAccessInterface {

    // username -> cooking list
    private final Map<String, List<Recipe>> cookingListByUser = new HashMap<>();

    @Override
    public void addRecipe(String userName, Recipe recipe) {
        final List<Recipe> cookingList =
                cookingListByUser.computeIfAbsent(userName, u -> new ArrayList<>());

        if (recipe != null && cookingList.stream().noneMatch(r -> r.getId() == recipe.getId())) {
            cookingList.add(recipe);
        }
    }

    @Override
    public List<Recipe> getAllRecipes(String userName) {
        return new ArrayList<>(cookingListByUser.getOrDefault(userName, new ArrayList<>()));
    }

    @Override
    public void deleteRecipe(String userName, int recipeID) {
        final List<Recipe> cookingList =
                cookingListByUser.getOrDefault(userName, new ArrayList<>());

        cookingList.removeIf(r -> r.getId() == recipeID);
    }
}
