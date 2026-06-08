package labyrinth.model.strategy.simulation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import labyrinth.service.serialization.DeckDTO;
import labyrinth.service.serialization.PlayerDTO;

public class PlayerSimulation {
  private final int index;
  private final List<Integer> drawPile = new LinkedList<>();
  private final List<Integer> discardPile = new LinkedList<>();
  private final List<Integer> drawPileReadOnly = Collections.unmodifiableList(drawPile);
  private final List<Integer> discardPileReadOnly = Collections.unmodifiableList(discardPile);

  public PlayerSimulation(PlayerDTO playerDTO, int index) {
    this.index = index;
    DeckDTO deckDTO = playerDTO.getDeck();
    this.drawPile.addAll(deckDTO.getDrawPile());
    this.discardPile.addAll(deckDTO.getDiscardPile());
  }

  public int getIndex() {
    return index;
  }

  public int getNextTreasure() {
    return drawPile.isEmpty() ? -1 : drawPile.getFirst();
  }

  public List<Integer> getDrawPile() {
    return drawPileReadOnly;
  }

  public List<Integer> getDiscardPile() {
    return discardPileReadOnly;
  }

  void discard() {
    int treasure = drawPile.removeFirst();
    discardPile.add(treasure);
  }

  void cancelDiscard() {
    int treasure = discardPile.removeLast();
    drawPile.addFirst(treasure);
  }
}
