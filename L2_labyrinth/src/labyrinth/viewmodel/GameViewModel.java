package labyrinth.viewmodel;

import javafx.beans.value.ObservableValue;
import labyrinth.model.GameState;

import java.util.List;

public interface GameViewModel {

    ObservableValue<String> titleProperty() ;

    String getTitle();

    ObservableValue<GameState> stateProperty();

    GameState getState();

    MazeViewModel getMazeViewModel();

    List<PlayerViewModel> getPlayersViewModel();

    ObservableValue<Integer> currentPlayerProperty();

    int getCurrentPlayer();

    void startGame();

    MenuViewModel getMenuViewModel();

    UserProfilesViewModel getUserProfilesViewModel();

}
