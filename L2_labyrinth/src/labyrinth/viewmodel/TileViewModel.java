package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;

import java.util.OptionalInt;


public interface TileViewModel {

    ObservableValue<TileKind> kindProperty() ;

    TileKind getKind() ;

    ObservableValue<Direction> directionProperty() ;

    Direction getDirection() ;

    void rotate(int nbTimes);

    void setKind(TileKind kind);

    ObservableValue<Integer> currentPlayerProperty();

    int getCurrentPlay();

    ObservableValue<Integer> startingPositionProperty();

    OptionalInt getStartingPosition();

    ObservableValue<Integer> treasureProperty() ;

    OptionalInt getTreasure() ;




}
