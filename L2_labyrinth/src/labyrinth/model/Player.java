package labyrinth.model;

import labyrinth.service.serialization.PlayerDTO;

import java.util.UUID;

public class Player {
    private final int index;
    private final Deck deck;
    private UUID profileUuid;
    private boolean isBot ;

    public Player(int index) {
        this.index = index;
        this.deck = new Deck();
        this.profileUuid = null;
    }

    public Player(int index, UUID profileUuid){
        this.index = index ;
        this.deck = new Deck() ;
        this.profileUuid = profileUuid ;

    }

    public Player(int index,boolean isBot){
        this.index = index ;
        this.deck = new Deck() ;
        this.isBot = isBot ;
    }

    // renvoie null si joueur anonyme
    public UUID getProfileUuid(){
        return profileUuid ;
    }


    public boolean getIsBot(){
        return isBot ;
    }
    public int getIndex() {
        return index;
    }

    public Deck getDeck(){
        return deck;
    }

    public boolean hasWon(){
        return getDeck().isFinish();
    }

    public void updateFrom(PlayerDTO playerDTO){
        deck.updateFrom(playerDTO.getDeck());
        this.profileUuid = playerDTO.getProfileUuid();
    }
}
