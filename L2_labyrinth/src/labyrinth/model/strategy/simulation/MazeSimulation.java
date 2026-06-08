package labyrinth.model.strategy.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import labyrinth.model.Direction;
import labyrinth.model.MazeMove;
import labyrinth.model.StartingPosition;
import labyrinth.model.Treasure;
import labyrinth.service.serialization.MazeDTO;
import labyrinth.service.serialization.TileDTO;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.collection.UnmodifiableGrid;

public class MazeSimulation {
  private final Grid<TileSimulation> tiles;
  private final Grid<TileSimulation> tilesReadOnly;
  private final Set<Integer> allowedRows;
  private final Set<Integer> allowedColumns;
  private final TileSimulation[] startingTiles = new TileSimulation[GameSimulation.NB_PLAYERS];
  private final TileSimulation[] treasureTiles = new TileSimulation[GameSimulation.NB_TREASURES];
  private final TileSimulation[] playerTiles = new TileSimulation[GameSimulation.NB_PLAYERS];
  private TileSimulation extraTile;
  private MazeMove forbiddenMove;

  public MazeSimulation(MazeDTO mazeDTO) {
    Grid<TileDTO> tileDTOGrid = mazeDTO.getTiles();
    tiles = new ArrayGrid<>(tileDTOGrid.getWidth(), tileDTOGrid.getHeight());
    tilesReadOnly = new UnmodifiableGrid<>(tiles);
    for (Grid.Entry<TileDTO> tileEntry : tileDTOGrid) {
      Position position = tileEntry.position();
      TileDTO tileDTO = tileEntry.value();
      TileSimulation tile = createTile(tileDTO, position);
      tiles.set(position, tile);
    }

    List<Integer> fixRows = mazeDTO.getFixedRows();
    List<Integer> fixColumns = mazeDTO.getFixedColumns();
    allowedRows =
        IntStream.range(0, tiles.getHeight())
            .filter(i -> !fixRows.contains(i))
            .boxed()
            .collect(Collectors.toSet());
    allowedColumns =
        IntStream.range(0, tiles.getWidth())
            .filter(i -> !fixColumns.contains(i))
            .boxed()
            .collect(Collectors.toSet());

    extraTile = createTile(mazeDTO.getExtraTile());
    forbiddenMove = mazeDTO.getForbiddenMove();
  }

  private TileSimulation createTile(TileDTO tileDTO, Position position) {
    TileSimulation tile = new TileSimulation(tileDTO, position);
    switch (tileDTO.getContent()) {
      case StartingPosition(int index) -> startingTiles[index] = tile;
      case Treasure(int id) -> treasureTiles[id] = tile;
      default -> {}
    }
    for (int playerIndex : tileDTO.getPlayers()) {
      playerTiles[playerIndex] = tile;
    }
    return tile;
  }

  private TileSimulation createTile(TileDTO tileDTO) {
    return createTile(tileDTO, null);
  }

  public boolean isValidMove(MazeMove move) {
    if (move == null) {
      return false;
    }
    if (move.equals(forbiddenMove)) {
      return false;
    }
    return switch (move.direction()) {
      case UP, DOWN -> allowedColumns.contains(move.index());
      case RIGHT, LEFT -> allowedRows.contains(move.index());
    };
  }

  void rotateExtraTile(int nbTimes) {
    extraTile.rotate(nbTimes);
  }

  List<MazeMove> getValidMazeMoves() {
    List<MazeMove> moves = new ArrayList<>();
    allowedRows.forEach(i -> {
      moves.add(new MazeMove(Direction.RIGHT, i));
      moves.add(new MazeMove(Direction.LEFT, i));
    });
    allowedColumns.forEach(i -> {
      moves.add(new MazeMove(Direction.UP, i));
      moves.add(new MazeMove(Direction.DOWN, i));
    });
    moves.remove(forbiddenMove);
    return moves;
  }

  void move(MazeMove move) {
    int index = move.index();
    TileSimulation initialTile = extraTile;
    switch (move.direction()) {
      case UP -> {
        for (int y = tiles.getHeight() - 1; y >= 0; y--) {
          extraTile = setTile(index, y, extraTile);
        }
      }
      case RIGHT -> {
        for (int x = 0; x < tiles.getWidth(); x++) {
          extraTile = setTile(x, index, extraTile);
        }
      }
      case DOWN -> {
        for (int y = 0; y < tiles.getHeight(); y++) {
          extraTile = setTile(index, y, extraTile);
        }
      }
      case LEFT -> {
        for (int x = tiles.getWidth() - 1; x >= 0; x--) {
          extraTile = setTile(x, index, extraTile);
        }
      }
    }
    for (int i = 0; i < playerTiles.length; i++) {
      if (playerTiles[i] == extraTile) {
        playerTiles[i] = initialTile;
      }
    }
    forbiddenMove = move.getOpposite();
  }

  private TileSimulation setTile(int x, int y, TileSimulation tile) {
    TileSimulation oldTile = tiles.get(x, y);
    tiles.set(x, y, tile);
    oldTile.setPosition(null);
    tile.setPosition(new Position(x, y));
    return oldTile;
  }

  public Grid<TileSimulation> getTiles() {
    return tilesReadOnly;
  }

  public TileSimulation getStartingTile(PlayerSimulation player) {
    return startingTiles[player.getIndex()];
  }

  public TileSimulation getTreasureTile(int id) {
    return treasureTiles[id];
  }

  public TileSimulation getPlayerTile(PlayerSimulation player) {
    return playerTiles[player.getIndex()];
  }

  void setPlayerTile(int index, TileSimulation tile) {
    playerTiles[index] = tile;
  }
}
