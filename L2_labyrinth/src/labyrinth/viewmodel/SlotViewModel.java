package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;

import labyrinth.model.MazeMove;


public interface SlotViewModel {
    MazeMove getMazeMove();

    TileViewModel getExtraTileViewModel();

    ObservableValue<Boolean> magnetizedProperty();

    void magnetize();

    void demagnetize();

    boolean isMagnetized() ;

    ObservableValue<Boolean> forbiddenProperty();

    boolean isForbidden();

    void moveMaze();

    ObservableValue<Boolean> activeProperty();

    boolean isActive();

    void setActive(boolean value);
}
