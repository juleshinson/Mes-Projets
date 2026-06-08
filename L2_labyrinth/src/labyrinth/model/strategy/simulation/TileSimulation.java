package labyrinth.model.strategy.simulation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import labyrinth.model.Content;
import labyrinth.model.Direction;
import labyrinth.service.serialization.TileDTO;
import labyrinth.util.collection.Position;

public class TileSimulation {
  private final Set<Direction> exits = new HashSet<>();
  private final Set<Direction> exitsReadOnly = Collections.unmodifiableSet(exits);
  private final Content content;
  private Position position;

  public TileSimulation(TileDTO tileDTO, Position position) {
    tileDTO
        .getTileKind()
        .getExits()
        .forEach(d -> exits.add(d.getRotated(tileDTO.getOrientation())));
    this.content = tileDTO.getContent();
    this.position = position;
  }

  public Content getContent() {
    return content;
  }

  public void rotate(int nbTimes) {
    List<Direction> tmp = exits.stream().map(d -> d.getRotated(nbTimes)).toList();
    exits.clear();
    exits.addAll(tmp);
  }

  public Set<Direction> getExits() {
    return exitsReadOnly;
  }

  public Position getPosition() {
    return position;
  }

  void setPosition(Position position) {
    this.position = position;
  }
}
