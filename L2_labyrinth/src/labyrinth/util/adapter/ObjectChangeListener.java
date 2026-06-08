package labyrinth.util.adapter;

import java.lang.ref.WeakReference;
import javafx.beans.property.ReadOnlyObjectWrapper;
import labyrinth.util.observable.ChangeListener;
import labyrinth.util.observable.ObservableValue;

public class ObjectChangeListener<T> implements ChangeListener<T> {
  private final WeakReference<ReadOnlyObjectWrapper<T>> wrapperRef;

  public ObjectChangeListener(ReadOnlyObjectWrapper<T> wrapper) {
    wrapperRef = new WeakReference<>(wrapper);
  }

  @Override
  public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
    ReadOnlyObjectWrapper<T> wrapper = wrapperRef.get();
    if (wrapper == null) {
      observable.removeListener(this);
    } else {
      wrapper.setValue(newValue);
    }
  }
}
