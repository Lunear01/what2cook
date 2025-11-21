package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class ViewModel<T> {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;
    private final String viewName;

    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public void setState(T state) {
        this.state = state;
        support.firePropertyChange(viewName, null, state);
    }

    public T getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
