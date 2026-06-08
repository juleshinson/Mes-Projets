package labyrinth.model.strategy;

import labyrinth.service.serialization.GameDTO;

public interface PlayStrategy {
  Move getNextMove(GameDTO game);
}
