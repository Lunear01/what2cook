package dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Recipe;
import use_case.add_favorite_list.AddFavoriteRecipeDataAccessInterface;

/**
 * In-memory implementation of the favorite recipe data access.
 * Stores favorites per user in a simple map.
 */
public class InMemoryFavoriteRecipeDataAccess
        implements AddFavoriteRecipeDataAccessInterface {

    // username -> favorites list
    private final Map<String, List<Recipe>> favoritesByUser = new HashMap<>();

    @Override
    public List<Recipe> getFavorites(String username) {
        return favoritesByUser.getOrDefault(username, new ArrayList<>());
    }

    @Override
    public void addToFavorites(String username, Recipe recipe) {
        final List<Recipe> favorites =
                favoritesByUser.computeIfAbsent(username, uuu -> new ArrayList<>());

        if (recipe != null && favorites.stream().noneMatch(rrr -> rrr.getId() == recipe.getId())) {
            favorites.add(recipe);
        }
    }
}
