package labyrinth.service.serialization;

import labyrinth.model.Deck;

import java.util.List;

public class DeckDTO {
    private List<Integer> drawPile ;
    private List<Integer> discardPile;

    public DeckDTO(Deck deck){
        this.drawPile = deck.getDrawPile() ;
        this.discardPile = deck.getDiscardPile() ;
    }

    public List<Integer> getDrawPile(){
        return this.drawPile ;
    }

    public List<Integer> getDiscardPile(){
        return this.discardPile ;
    }
}
