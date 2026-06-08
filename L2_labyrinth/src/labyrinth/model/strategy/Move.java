package labyrinth.model.strategy;

import labyrinth.model.MazeMove;
import labyrinth.util.collection.Position;

public record Move(int nbRotations, MazeMove mazeMove, Position destination) {}
