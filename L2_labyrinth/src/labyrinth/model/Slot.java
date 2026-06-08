package labyrinth.model;

import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;

import java.lang.IllegalStateException;
import java.util.UUID;

public class Slot {
    private final int index;
    private final Game game;
    private final SimpleObservableValue<Player> player;


    public Slot(Game game, int index) {
        this.index = index;
        this.game = game ;
        this.player = new SimpleObservableValue<>(null);
    }

    public int getIndex() {
        return index;
    }

    public Player getPlayer() {
        return player.getValue();
    }

    ObservableValue<Player> currentPlayerProperty() {
        return player.asReadOnly();
    }


    public void setupIfNeeded() {
        if (game.getState() != GameState.SETUP) {
            game.hardReset();
        }
    }

    public void open(){
        if(game.getState() != GameState.SETUP ){
            throw new IllegalStateException();
        }
        this.player.setValue(new Player(this.getIndex()));
    }

    public void open(boolean isBot){
        if(game.getState() != GameState.SETUP ){
            throw new IllegalStateException();
        }
        this.player.setValue(new Player(this.getIndex(),isBot));
    }

    // nouvelle methode TP10 ; ouvre avec un joueur lié a un profil
    public void open(UUID profileUuid){
        if(game.getState() != GameState.SETUP ){
            throw new IllegalStateException();
        }
        this.player.setValue(new Player(this.getIndex(), profileUuid));
    }

    public void open(UUID profileUuid, boolean isBot){
        if(game.getState() != GameState.SETUP ){
            throw new IllegalStateException();
        }
        this.player.setValue(new Player(this.getIndex(),isBot));
    }

    public void close(){
        if(game.getState() != GameState.SETUP ){
            throw new IllegalStateException();
        }
        if(this.player.getValue() == null){
            throw new IllegalStateException();
        }
        this.player.setValue(null);
    }
}
