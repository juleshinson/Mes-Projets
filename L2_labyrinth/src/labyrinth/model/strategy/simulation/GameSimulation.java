package labyrinth.model.strategy.simulation;

import java.util.ArrayList;
import java.util.List;
import labyrinth.model.MazeMove;
import labyrinth.model.Treasure;
import labyrinth.model.strategy.Move;
import labyrinth.service.serialization.GameDTO;
import labyrinth.service.serialization.PlayerDTO;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.Graph;
import labyrinth.util.graph.Paths;

/**
 * Lightweight and reversible game engine dedicated to AI simulations.
 *
 * <p>This model is independent of the main game model and is optimized for fast exploration of
 * hypothetical moves. Simulations are performed by mutating the current state and cancelling
 * modifications afterward.
 *
 * <p>A full simulated turn is always composed of:
 *
 * <ol>
 *   <li>a maze modification ({@code modifyMaze}),
 *   <li>a player move ({@code movePlayer}),
 *   <li>optionally a rollback using the corresponding cancel methods.
 * </ol>
 *
 * <p>This class assumes disciplined usage: every applied modification must be reverted in reverse
 * order.
 */
public class GameSimulation {
  public static final int NB_PLAYERS = 4;
  public static final int NB_TREASURES = 24;
  private static final int DEFAULT_DISTANCE = 15;

  private final List<PlayerSimulation> players = new ArrayList<>();
  private int currentPlayerIndex;
  private final MazeSimulation maze;
  private final List<FullMove> moves = new ArrayList<>();
  private MazeModification mazeModification;

  /**
   * Creates a new {@code GameSimulation} in the state encoded by {@code gameDTO}.
   *
   * @param gameDTO the starting state of the simulation
   */
  public GameSimulation(GameDTO gameDTO) {
    for (int i = 0; i < NB_PLAYERS; i++) {
      PlayerDTO playerDTO = gameDTO.getPlayers().get(i);
      PlayerSimulation player = playerDTO == null ? null : new PlayerSimulation(playerDTO, i);
      players.add(player);
    }
    maze = new MazeSimulation(gameDTO.getMaze());
    currentPlayerIndex = gameDTO.getCurrentPlayerIndex();
  }

  public PlayerSimulation getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public TileSimulation getCurrentPlayerTile() {
    return getMaze().getPlayerTile(getCurrentPlayer());
  }

  public MazeSimulation getMaze() {
    return maze;
  }

  public Paths<TileSimulation> computePaths() {
    Graph<TileSimulation> graph = MazeSimulationAdapter.getGraph(getMaze());
    TileSimulation from = getMaze().getPlayerTile(getCurrentPlayer());
    return new Paths<>(graph, from);
  }

  /**
   * Applies a full move to the game.
   *
   * <p>This is equivalent to successively call {@code modifyMaze} and {@code movePlayer}.
   *
   * @param move the move to apply
   */
  public void apply(Move move) {
    modifyMaze(move.nbRotations(), move.mazeMove());
    movePlayer(move.destination());
  }

  /**
   * Applies the specified modifications to the maze.
   *
   * <p>The simulation alternates between maze modifications and player moves. A maze modification
   * must always be completed by a player move before another modification can occur.
   *
   * @param nbRotations the number of rotations of the extra tile to apply
   * @param mazeMove the maze move to apply
   */
  public void modifyMaze(int nbRotations, MazeMove mazeMove) {
    if (mazeModification != null) {
      throw new IllegalStateException("Modification has already been called");
    }
    getMaze().rotateExtraTile(nbRotations);
    getMaze().move(mazeMove);
    mazeModification = new MazeModification(nbRotations, mazeMove);
  }

  /**
   * Returns all the valid maze moves in the current state of the maze.
   *
   * @return all the valid maze moves in the current state of the maze
   */
  public List<MazeMove> getValidMazeMoves() {
    return maze.getValidMazeMoves();
  }

  /**
   * Move the current player to the specified tile.
   *
   * <p>The simulation alternates between maze modifications and player moves. A maze modification
   * must always be completed by a player move before another modification can occur.
   *
   * @param tile the new tile of the current player
   */
  public void movePlayer(TileSimulation tile) {
    if (mazeModification == null) {
      throw new IllegalStateException("Modification has not been called");
    }
    TileSimulation origin = getCurrentPlayerTile();
    movePlayerRaw(tile);
    boolean treasureFound = updatePlayerTreasures(tile);
    PlayerMove playerMove = new PlayerMove(origin, tile, treasureFound);
    FullMove fullMove = new FullMove(mazeModification, playerMove);
    mazeModification = null;
    moves.add(fullMove);
    currentPlayerIndex = getNextPlayerIndex();
  }

  /**
   * Move the current player to the specified position.
   *
   * <p>This method must alternate with {@code modifyMaze} during a simulation.
   *
   * @param position the new position of the current player
   */
  public void movePlayer(Position position) {
    TileSimulation tile = getMaze().getTiles().get(position);
    movePlayer(tile);
  }

  private void movePlayerRaw(TileSimulation tile) {
    getMaze().setPlayerTile(currentPlayerIndex, tile);
  }

  /**
   * Undoes the last full move of this simulation.
   *
   * <p>This is equivalent to successively call {@code undoLastPlayerMove} and {@code
   * undoLastMazeModification}. It is an error to call this method when a maze modification has
   * occurred without a player move.
   */
  public void undoLastMove() {
    undoLastPlayerMove();
    undoLastMazeModification();
  }

  /**
   * Undoes the last player move of this simulation.
   *
   * <p>It is an error to call this method when the last call was not a {@code movePlayer}.
   */
  public void undoLastPlayerMove() {
    if (moves.isEmpty()) {
      throw new IllegalStateException("No moves to cancel");
    }
    if (mazeModification != null) {
      throw new IllegalStateException("Modification pending");
    }
    FullMove lastMove = moves.removeLast();
    currentPlayerIndex = getPreviousPlayerIndex();
    movePlayerRaw(lastMove.playerMove().origin());
    if (lastMove.playerMove().treasureFound()) {
      getCurrentPlayer().cancelDiscard();
    }
    mazeModification = lastMove.mazeModification();
  }

  /**
   * Undoes the last maze modification of this simulation.
   *
   * <p>It is an error to call this method when the last call was not a {@code modifyMaze}.
   */
  public void undoLastMazeModification() {
    if (mazeModification == null) {
      throw new IllegalStateException("No modification pending");
    }
    getMaze().move(mazeModification.mazeMove().getOpposite());
    getMaze().rotateExtraTile(-mazeModification.nbRotations());
    mazeModification = null;
  }

  private boolean updatePlayerTreasures(TileSimulation tile) {
    PlayerSimulation currentPlayer = getCurrentPlayer();
    int nextTreasure = currentPlayer.getNextTreasure(); // guard == -1
    boolean treasureFound = tile.getContent() instanceof Treasure(int id) && nextTreasure == id;
    if (treasureFound) {
      currentPlayer.discard();
    }
    return treasureFound;
  }

  public int getNextPlayerIndex() {
    int index = currentPlayerIndex;
    do {
      index = (index + 1) % NB_PLAYERS;
    } while (players.get(index) == null);
    return index;
  }

  public int getPreviousPlayerIndex() {
    int index = currentPlayerIndex;
    do {
      index = (NB_PLAYERS + index - 1) % NB_PLAYERS;
    } while (players.get(index) == null);
    return index;
  }

  public List<PlayerSimulation> getPlayers() {
    return players;
  }

  public boolean hasWon(PlayerSimulation player) {
    return player.getDrawPile().isEmpty()
        && maze.getPlayerTile(player) == maze.getStartingTile(player);
  }

  public boolean isGameOver() {
    for (PlayerSimulation player : players) {
      if (player != null && hasWon(player)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the Manhattan distance between the given player and its target.
   *
   * <p>The target of a player is its next treasure to find or its starting position if there are
   * none.
   *
   * @param player the player
   * @return the Manhattan distance between the given player and its target
   */
  public int getDistanceToTarget(PlayerSimulation player) {
    Position playerPosition = maze.getPlayerTile(player).getPosition();
    return getDistanceToTarget(playerPosition, player);
  }

  /**
   * Returns the Manhattan distance between the given position and the target of the given player.
   *
   * <p>The target of a player is its next treasure to find or its starting position if there are
   * none.
   *
   * @param position the position
   * @param player the player
   * @return the Manhattan distance between the given position and the target of the given player
   */
  public int getDistanceToTarget(Position position, PlayerSimulation player) {
    int nextTreasure = player.getNextTreasure();
    TileSimulation target =
        nextTreasure == -1 ? maze.getStartingTile(player) : maze.getTreasureTile(nextTreasure);
    Position targetPosition = target.getPosition();
    return targetPosition == null ? DEFAULT_DISTANCE : getDistance(position, targetPosition);
  }

  private int getDistance(Position position1, Position position2) {
    return Math.abs(position2.x() - position1.x()) + Math.abs(position2.y() - position1.y());
  }



  private record MazeModification(int nbRotations, MazeMove mazeMove) {}

  private record PlayerMove(TileSimulation origin, TileSimulation target, boolean treasureFound) {}

  private record FullMove(MazeModification mazeModification, PlayerMove playerMove) {}
}
