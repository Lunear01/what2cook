package dataaccess;

import entity.Recipe;
import use_case.cookinglist.CookingListDataAccessInterface;

import java.util.*;

public class InMemoryCookingListDataAccess implements CookingListDataAccessInterface {

    // username -> list of recipes
    private final Map<String, List<Recipe>> cookingLists = new HashMap<>();

    @Override
    public synchronized List<Recipe> getCookingList(String username) {
        return new ArrayList<>(cookingLists.getOrDefault(username, Collections.emptyList()));
    }

    @Override
    public synchronized void addToCookingList(String username, Recipe recipe) {
        final List<Recipe> list =
                cookingLists.computeIfAbsent(username, u -> new ArrayList<>());
        final boolean exists = list.stream()
                .anyMatch(r -> r.getId() == recipe.getId());

        if (!exists) {
            list.add(recipe);
        }
    }
}

