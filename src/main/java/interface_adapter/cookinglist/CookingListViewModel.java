package interface_adapter.cookinglist;

import java.util.List;

import entity.Recipe;
import interface_adapter.ViewModel;

public class CookingListViewModel extends ViewModel<CookingListState> {

    public static final String VIEW_NAME = "cooking list";

    public CookingListViewModel() {
        super(VIEW_NAME);
        this.setState(new CookingListState());
    }

    /**
     * Updates the personal cooking list in the view model and
     * notifies listeners that the cooking list has changed.
     *
     * @param recipes the new personal cooking list.
     */
    public void setPersonalCookingList(List<Recipe> recipes) {
        final CookingListState state = getState();
        state.setPersonalCookingList(recipes);
        setState(state);
        firePropertyChanged("cooking");
    }

    public List<Recipe> getPersonalCookingList() {
        return getState().getPersonalCookingList();
    }

    /**
     * Sets the status message in the view model and notifies listeners
     * that the status message has changed.
     *
     * @param message the new status message.
     */
    public void setStatusMessage(String message) {
        final CookingListState state = getState();
        state.setStatusMessage(message);
        setState(state);
        firePropertyChanged("statusMessage");
    }

    public String getStatusMessage() {
        return getState().getStatusMessage();
    }
}
