package interface_adapter.fridgemodify;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the fridge view.
 * Holds a FridgeState and notifies listeners when it changes.
 */
public class FridgeViewModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private FridgeState state = new FridgeState();

    /**
     * Adds a property change listener to observe state updates.
     *
     * @param listener the listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Returns the current state.
     *
     * @return the current FridgeState.
     */
    public FridgeState getState() {
        return state;
    }

    /**
     * Sets a new state and fires a property change event.
     *
     * @param newState the new state to set.
     */
    public void setState(FridgeState newState) {
        final FridgeState oldState = this.state;
        this.state = newState;
        pcs.firePropertyChange("fridge", oldState, newState);
    }

    /**
     * Convenience helper to update only the ingredient list.
     * Creates a new FridgeState with the given ingredients.
     *
     * @param ingredients the updated ingredient list.
     */
    public void update(List<String> ingredients) {
        final FridgeState newState = new FridgeState(new ArrayList<>(ingredients));
        setState(newState);
    }
}
