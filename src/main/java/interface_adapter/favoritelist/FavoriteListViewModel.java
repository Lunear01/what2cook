package interface_adapter.favoritelist;

import java.util.List;

import entity.Recipe;
import interface_adapter.ViewModel;

/**
 * ViewModel for the favorite recipes view.
 * Wraps FavoriteListState and notifies listeners on changes.
 */
public class FavoriteListViewModel extends ViewModel<FavoriteListState> {

    public static final String VIEW_NAME = "favorite list";

    public FavoriteListViewModel() {
        super(VIEW_NAME);
        this.setState(new FavoriteListState());
    }

    /**
     * Updates the favorite list in the view model and
     * notifies listeners that the favorites have changed.
     *
     * @param recipes the new favorite recipes list.
     */
    public void setFavoriteList(List<Recipe> recipes) {
        final FavoriteListState state = getState();
        state.setFavoriteList(recipes);
        setState(state);
        // property 名你可以自定义, 对应 View 里加监听
        firePropertyChanged("favorites");
    }

    public List<Recipe> getFavoriteList() {
        return getState().getFavoriteList();
    }

    /**
     * Sets the status message in the view model and notifies listeners
     * that the status message has changed.
     *
     * @param message the new status message.
     */
    public void setStatusMessage(String message) {
        final FavoriteListState state = getState();
        state.setStatusMessage(message);
        setState(state);
        firePropertyChanged("statusMessage");
    }

    public String getStatusMessage() {
        return getState().getStatusMessage();
    }
}
