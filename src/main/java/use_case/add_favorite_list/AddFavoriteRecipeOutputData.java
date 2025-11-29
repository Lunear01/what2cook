package use_case.add_favorite_list;

import entity.Recipe;

import java.util.List;

public class AddFavoriteRecipeOutputData {

    private final List<Recipe> favorites;
    private final String message;

    public AddFavoriteRecipeOutputData(List<Recipe> favorites, String message) {
        this.favorites = favorites;
        this.message = message;
    }

    public List<Recipe> getFavorites() {
        return favorites;
    }

    public String getMessage() {
        return message;
    }
}
