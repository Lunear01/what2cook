package interface_adapter.fridgemodify;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.stream.Collectors;

import entity.Ingredient;


public class FridgeViewModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void update(List<Ingredient> ingredients) {
        final List<String> names = ingredients.stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());

        pcs.firePropertyChange("fridge", null, new FridgeState(names));
    }
}
