package labyrinth.service.serialization;

import labyrinth.model.Player;

import java.util.UUID;

public class PlayerDTO {
    private final int index ;
    private final DeckDTO deck ;
    private final UUID profileUuid ;
    private final boolean isBot ;

    public PlayerDTO(Player player){
        this.index = player.getIndex() ;
        this.deck = new DeckDTO(player.getDeck()) ;
        this.profileUuid = player.getProfileUuid() ;
        this.isBot = player.getIsBot();
    }

    public int getIndex(){
        return this.index ;
    }

    public DeckDTO getDeck(){
        return deck ;
    }

    public UUID getProfileUuid(){
        return profileUuid ;
    }

    public boolean getIsBot(){
        return isBot ;
    }
}
