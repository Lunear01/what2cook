package interface_adapter.favoritelist;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * State for the favorite recipes view.
 * Holds the current favorites list and a status message.
 */
public class FavoriteListState {

    // 用户收藏的菜谱列表
    private List<Recipe> favoriteList = new ArrayList<>();

    // 状态提示信息，比如 "XXX added to your favorites"
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
