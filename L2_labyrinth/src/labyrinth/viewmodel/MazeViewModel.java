package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;
import labyrinth.model.MazeMove;
import labyrinth.util.collection.Grid;

import java.util.Map;

public interface MazeViewModel {
    
    Grid<CellViewModel> getCellViewModels();

    TileViewModel getExtraTileViewModel() ;

    void rotateExtraTile(int nbTimes) ;

    Map<MazeMove,SlotViewModel> getSlotViewModels();


    MazeMove getForbiddenMove() ;

    ObservableValue<MazeMove> forbiddenMoveProperty();

    void setForbiddenMove(MazeMove move);

    void moveMaze(MazeMove move);

    ObservableValue<GameState> stateProperty();

    GameState getState();


}
