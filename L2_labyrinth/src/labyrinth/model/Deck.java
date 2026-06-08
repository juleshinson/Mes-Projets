package labyrinth.model;

import labyrinth.service.serialization.DeckDTO;
import labyrinth.util.observable.ObservableList;
import labyrinth.util.observable.SimpleObservableList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final SimpleObservableList<Integer> drawPile;
    private final SimpleObservableList<Integer> discardPile;

    public Deck(){
        this.drawPile = new SimpleObservableList<>() ;
        this.discardPile = new SimpleObservableList<>() ;
    }

    public ObservableList<Integer> drawPileProperty(){
        return drawPile;
    }

    public List<Integer> getDrawPile() {
        return Collections.unmodifiableList(drawPile);
    }

    public ObservableList<Integer> discardPileProperty(){
        return discardPile;
    }

    public List<Integer> getDiscardPile() {
        return Collections.unmodifiableList(discardPile);
    }

    public void init(List<Integer> treasures){
        this.drawPile.clear();
        this.drawPile.addAll(treasures);
        this.discardPile.clear();
    }

    public void clear(){
        this.drawPile.clear();
        this.discardPile.clear();
    }

    public void discard(){
        this.discardPile.add(this.drawPile.removeFirst()) ;
    }

    public boolean isFinish(){
        return this.drawPile.isEmpty() ;
    }

    public void updateFrom(DeckDTO deckDTO){
        drawPile.clear();
        drawPile.addAll(deckDTO.getDrawPile());

        discardPile.clear();
        discardPile.addAll(deckDTO.getDiscardPile()) ;
    }

}
