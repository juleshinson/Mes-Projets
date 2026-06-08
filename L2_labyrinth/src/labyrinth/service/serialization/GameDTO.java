package labyrinth.service.serialization;

import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.model.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameDTO {
    private final int getCurrentPlayerIndex ;
    private final GameState gameState ;
    private final List<PlayerDTO> players ;
    private final MazeDTO maze;
    private final UUID uuid ;

    public GameDTO(Game game){
        this.getCurrentPlayerIndex = game.getCurrentPlayer().getIndex();
        this.gameState = game.getState() ;
        this.uuid = game.getUuid();
        this.players = new ArrayList<>();
        for(int i = 0 ; i < 4 ; i++){
            Slot slot = game.getSlot(i);
            // getSlot peut retourner null (si slots.length < 4) et getPlayer() peut être null (slot fermé)
            if(slot != null && slot.getPlayer() != null){
                players.add(i, new PlayerDTO(slot.getPlayer()));
            }else{
                players.add(i, null);
            }
        }
        this.maze = new MazeDTO(game.getMaze());
    }

    public GameState getState(){
        return gameState ;
    }

    public List<PlayerDTO> getPlayers(){
        return players ;
    }

    public Integer getCurrentPlayerIndex(){
        return getCurrentPlayerIndex ;
    }

    public MazeDTO getMaze(){
        return maze ;
    }

    public UUID getUuid(){
        return uuid ;
    }
}
