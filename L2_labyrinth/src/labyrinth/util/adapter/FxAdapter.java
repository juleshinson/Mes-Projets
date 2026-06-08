package labyrinth.util.adapter;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import labyrinth.util.observable.ObservableList;
import labyrinth.util.observable.ObservableValue;

public final class FxAdapter {
  private FxAdapter() {}

  public static <T> javafx.beans.value.ObservableValue<T> adapt(
      ObservableValue<? extends T> observable) {
    ReadOnlyObjectWrapper<T> wrapper = new ReadOnlyObjectWrapper<>(observable.getValue());
    observable.addListener(new ObjectChangeListener<>(wrapper));
    return wrapper.getReadOnlyProperty();
  }

  public static <T> javafx.collections.ObservableList<T> adapt(
      ObservableList<? extends T> observable) {
    javafx.collections.ObservableList<T> wrapper = FXCollections.observableArrayList(observable);
    observable.addListener(new ListInvalidationListener<>(observable, wrapper));
    return FXCollections.unmodifiableObservableList(wrapper);
  }
}
