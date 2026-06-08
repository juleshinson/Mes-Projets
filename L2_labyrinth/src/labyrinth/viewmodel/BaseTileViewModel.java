package labyrinth.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;

import java.util.OptionalInt;

public class BaseTileViewModel implements TileViewModel{
    private final Property<TileKind> kind = new SimpleObjectProperty<>(TileKind.CORRIDOR);
    private final Property<Direction> direction = new SimpleObjectProperty<>(Direction.DOWN);
    private final IntegerProperty startingPositionProperty = new SimpleIntegerProperty(0);
    private final Property<Integer> treasureProperty = new SimpleObjectProperty<>(-1);
    private final Property<Integer> currentPlayerProperty = new SimpleObjectProperty<>(-1);


    public void setKind(TileKind kind){
        this.kind.setValue(kind);

    }

    public ObservableValue<Integer> currentPlayerProperty() {
        return currentPlayerProperty;
    }

    public void setCurrentPlayer(int idx) { currentPlayerProperty.setValue(idx); }


    public int getCurrentPlay() {
        return currentPlayerProperty.getValue();
    }

    public ObservableValue<Integer> startingPositionProperty() {
        return startingPositionProperty.asObject();
    }


    public OptionalInt getStartingPosition() {
        return OptionalInt.empty();
    }

    public ObservableValue<Integer> treasureProperty() {
        return treasureProperty;
    }

    public void setTreasure(int id) { treasureProperty.setValue(id); }

    public OptionalInt getTreasure() {
        int val = treasureProperty.getValue();
        return val >= 0 ? OptionalInt.of(val) : OptionalInt.empty();
    }

    public void setDirection(Direction direction){
        this.direction.setValue(direction) ;
    }


    public void rotate(int nbTimes){
        Direction direction = getDirection().getRotated(nbTimes);
        setDirection(direction);
    }


    public ObservableValue<TileKind> kindProperty() {
        return kind;
    }


    public TileKind getKind() {
        return kind.getValue();
    }

    public ObservableValue<Direction> directionProperty() {
        return direction;
    }


    public Direction getDirection() {
        return direction.getValue();
    }
}


