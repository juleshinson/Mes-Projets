package labyrinth.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.service.Services;
import labyrinth.view.DialogHandler;

import java.util.ArrayList;
import java.util.List;

public class BaseGameViewModel implements GameViewModel{
    private final Game game ;
    private final BaseMazeViewModel mazeViewModel ;
    private final BaseMenuViewModel menuViewModel;
    private final List<PlayerViewModel> playerViewModels ;
    private final Property<String> title = new SimpleObjectProperty<>("Labyrinth");
    private final IntegerProperty currentPlayerId = new SimpleIntegerProperty();
    private final Property<GameState> stateProperty = new SimpleObjectProperty<>(GameState.SETUP);
    private final Services services;
    private final UserProfilesViewModel userProfilesViewModel;

    public BaseGameViewModel(Game game) {
        this(game, null);
    }

    public BaseGameViewModel(Game game, Services services) {
        this.game = game ;
        this.services = services;
        this.mazeViewModel = new BaseMazeViewModel(game) ;
        // on passe les services au menuViewModel car c'est lui qui fait save/load
        this.menuViewModel = new BaseMenuViewModel(game, services);
        this.playerViewModels = new ArrayList<>(4);
        for(int i = 0 ; i < 4 ; i ++){
            this.playerViewModels.add(new BasePlayerViewModel(i, services,game.getSlot(i)));
        }
        this.userProfilesViewModel = new BaseUserProfilesViewModel(game, services);
        game.stateProperty().addListener((obs, oldEtat, nouvelEtat) -> {
            // currentPlayerId mis à jour avant stateProperty pour que updateView() dans MainView
            // lise déjà le bon index quand il est appelé depuis stateProperty.setValue()
            if (game.getCurrentPlayer() != null) {
                currentPlayerId.setValue(game.getCurrentPlayer().getIndex());
            }
            stateProperty.setValue(nouvelEtat) ;
            onEtatChange(nouvelEtat);
        });

    }

    public void setupDialogHandler(DialogHandler dialogHandler) {
        dialogHandler.setUserProfilesViewModel(userProfilesViewModel);
    }

    // appelé à chaque changement d'état du jeu
    private void onEtatChange(GameState nouvelEtat) {
        // si pas de service de dialogue on ne fait rien
        if (services == null || services.getDialogService() == null) {
            return;
        }
        if (nouvelEtat == GameState.GAME_OVER) {
            // on annonce le gagnant
            if (game.getCurrentPlayer() != null) {
                String msg = "Le joueur " + (game.getCurrentPlayer().getIndex() + 1) + " a gagné !";
                services.getDialogService().showMessage(msg);
            }
        }
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }


    public ObservableValue<String> titleProperty() {
        return title ;
    }

    public String getTitle() {
        return title.getValue();
    }


    public ObservableValue<GameState> stateProperty() {
        return stateProperty;
    }


    public GameState getState() {
        try {
            return stateProperty.getValue();
        } catch (Exception e) {
            System.out.println("null");
        }
        return stateProperty.getValue();
    }

    public MazeViewModel getMazeViewModel() {
        if(getState().equals(GameState.SETUP)){
            return null;
        }
        return mazeViewModel;
    }


    public ObservableValue<Integer> currentPlayerProperty() {
        return currentPlayerId.asObject();
    }


    public int getCurrentPlayer() {
        return currentPlayerId.getValue();

    }
    public void setStateProperty(GameState state){
        stateProperty.setValue(state);
    }


    public BaseMenuViewModel getMenuViewModel() {
        return menuViewModel ;
    }

    @Override
    public UserProfilesViewModel getUserProfilesViewModel() {
        return userProfilesViewModel;
    }


    public void startGame() {
        boolean hasActivePlayer = playerViewModels.stream()
                .anyMatch(PlayerViewModel::isActive);

        if (!hasActivePlayer) {
            System.out.println("Créer un joueur pour commencer");
            return;
        }

        game.reset() ; // remet le jeu en SETUP avant d'init
        game.init() ;

        for(PlayerViewModel player : this.playerViewModels){
            player.setLockedProperty(true);
        }
        // si le premier joueur est un bot, démarrer son tour immédiatement
        game.startTurn();
    }


    public List<PlayerViewModel> getPlayersViewModel() {
        return this.playerViewModels ;
    }

    public Services getServices() {
        return services;
    }
}
