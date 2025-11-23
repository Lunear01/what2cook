package interface_adapter.fridgemodify;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

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
     * Updates the ViewModel with the given list of ingredients.
     * Creates a new state instance and notifies property change listeners.
     *
     * @param ingredients the updated ingredient list.
     */
    public void update(List<String> ingredients) {

        final FridgeState oldState = this.state;
        final FridgeState newState = new FridgeState(new ArrayList<>(ingredients));

        this.state = newState;

        pcs.firePropertyChange("fridge", oldState, newState);
    }

    public FridgeState getState() {
        return state;
    }
}
