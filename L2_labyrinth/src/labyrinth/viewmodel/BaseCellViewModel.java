package labyrinth.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.*;
import labyrinth.util.adapter.FxAdapter;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.InvalidationListener;

public class BaseCellViewModel implements CellViewModel {

    private final BaseTileViewModel stubTileViewModel ;
    private final BooleanProperty activeProperty = new SimpleBooleanProperty(false) ;
    private final  Position position ;
    private Game game;
    private Tile currentTile;
    private javafx.beans.value.ObservableValue<Tile> fxTileObs = null; // garde le FxAdapter wrapper en vie
    private javafx.beans.value.ObservableValue<Direction> fxOrientation = null;
    private javafx.beans.value.ChangeListener<Direction> orientationListener = null;
    private javafx.beans.value.ObservableValue<Content> fxContent = null;
    private javafx.beans.value.ChangeListener<Content> contentListener = null;
    private InvalidationListener playersListener = null;

    public BaseCellViewModel(Position position){
        this.position = position ;
        this.stubTileViewModel = new BaseTileViewModel() ;
    }

    // nouveau constructeur avec tuile réelle utilisé par BaseMazeViewModel après init
    public BaseCellViewModel(Position position, ObservableValue<Tile> tileObs, Game game) {
        this.position = position;
        this.stubTileViewModel = new BaseTileViewModel();
        this.game = game;

        if (tileObs != null) {
            // garder la référence pour éviter le GC du wrapper FxAdapter
            this.fxTileObs = tileObs;
            // on met à jour le viewmodel de tuile à partir de la vraie tuile
            updateDepuisTuile(tileObs.getValue());

            // quand la tuile change (après un mouvement de labyrinthe) on met à jour
            tileObs.addListener((obs, oldTile, newTile) -> {
                updateDepuisTuile(newTile);
            });
        }
    }

    // met à jour le stubTileViewModel depuis une vraie tuile
    private void updateDepuisTuile(Tile tile) {
        if (tile == null) return;

        // retirer les anciens listeners avant d'en créer de nouveaux
        if (fxOrientation != null && orientationListener != null) {
            fxOrientation.removeListener(orientationListener);
        }
        if (fxContent != null && contentListener != null) {
            fxContent.removeListener(contentListener);
        }
        if (this.currentTile != null && playersListener != null) {
            this.currentTile.playersProperty().removeListener(playersListener);
        }

        this.currentTile = tile;
        stubTileViewModel.setKind(tile.getKind());
        stubTileViewModel.setDirection(tile.getOrientation());

        fxOrientation = FxAdapter.adapt(tile.orientationProperty());
        orientationListener = (obs, oldD, newD) -> stubTileViewModel.setDirection(newD);
        fxOrientation.addListener(orientationListener);

        // trésor : -1 = pas de trésor, id >= 0 = trésor présent (id=0→'A', id=23→'X')
        fxContent = FxAdapter.adapt(tile.contentProperty());
        stubTileViewModel.setTreasure(tile.getContent() instanceof Treasure t ? t.id() : -1);
        contentListener = (obs, oldC, newC) -> {
            stubTileViewModel.setTreasure(newC instanceof Treasure t ? t.id() : -1);
        };
        fxContent.addListener(contentListener);

        // joueur sur la tuile on met InvalidationListener sur la liste des joueurs
        java.util.List<Player> psInit = tile.getPlayers();
        stubTileViewModel.setCurrentPlayer(psInit.isEmpty() ? -1 : psInit.getFirst().getIndex());
        playersListener = obs -> {
            java.util.List<Player> ps = tile.getPlayers();
            stubTileViewModel.setCurrentPlayer(ps.isEmpty() ? -1 : ps.getFirst().getIndex());
        };
        tile.playersProperty().addListener(playersListener);
    }

    public BaseTileViewModel getStubTileView(){
        return this.stubTileViewModel ;
    }


    public Position getPosition() {
        return position;
    }


    public TileViewModel getTileViewModel() {
        return stubTileViewModel;
    }


    public ObservableValue<Boolean> activeProperty() {
        return activeProperty;
    }


    public boolean isActive() {
        return activeProperty.getValue();
    }

    public void setActive(boolean active) {
        activeProperty.setValue(active);
    }


    public void movePlayer() {

        if (game != null && currentTile != null && game.getState() == GameState.PLAYER_MOVE) {
            try{
                game.movePlayer(currentTile);
            }catch (Exception e) {
                System.out.println("movePlayer erreur : " + e.getMessage());
            }
        }else{
            System.out.println("Je déplace le joueur à " + getPosition());
        }
    }
}

