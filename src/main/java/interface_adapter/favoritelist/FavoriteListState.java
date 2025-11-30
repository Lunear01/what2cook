package interface_adapter.favoritelist;

import java.util.ArrayList;
import java.util.List;

import entity.Recipe;

/**
 * State for the favorite recipes view.
 * Holds the current favorites list and a status message.
 */
public class FavoriteListState {

    private List<Recipe> favoriteList = new ArrayList<>();

    private String statusMessage = "";

    public List<Recipe> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<Recipe> favoriteList) {
        this.favoriteList = favoriteList;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
