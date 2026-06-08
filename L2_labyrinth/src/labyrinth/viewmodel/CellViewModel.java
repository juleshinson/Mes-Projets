package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import labyrinth.util.collection.Position;

public interface CellViewModel {

    Position getPosition();

    TileViewModel getTileViewModel() ;

    ObservableValue<Boolean> activeProperty();

    boolean isActive();

    void movePlayer() ;


}
