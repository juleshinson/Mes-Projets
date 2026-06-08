package labyrinth.model.strategy.simulation;

import java.util.Optional;
import labyrinth.model.Direction;
import labyrinth.util.Require;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.Graph;

public class MazeSimulationAdapter extends Graph<TileSimulation> {
  private MazeSimulationAdapter() {}

  public static Graph<TileSimulation> getGraph(MazeSimulation maze) {
    Require.notNull(maze);
    Graph<TileSimulation> graph = new Graph<>();
    Grid<TileSimulation> grid = maze.getTiles();
    grid.forEach(entry -> graph.addNode(entry.value()));
    grid.forEach(
        entry ->
            entry.value().getExits().stream()
                .map(direction -> computeNeighbor(grid, entry, direction))
                .flatMap(Optional::stream)
                .forEach(neighbor -> graph.addEdge(entry.value(), neighbor)));
    return graph;
  }

  private static Optional<TileSimulation> computeNeighbor(
      Grid<TileSimulation> grid, Grid.Entry<TileSimulation> entry, Direction direction) {
    Position adjacentPosition = computeAdjacentPosition(entry.position(), direction);
    return getTile(grid, adjacentPosition)
        .filter(tile -> tile.getExits().contains(direction.getOpposite()));
  }

  private static Position computeAdjacentPosition(Position position, Direction direction) {
    int x = position.x();
    int y = position.y();
    switch (direction) {
      case UP -> y--;
      case RIGHT -> x++;
      case DOWN -> y++;
      case LEFT -> x--;
    }
    return new Position(x, y);
  }

  private static Optional<TileSimulation> getTile(Grid<TileSimulation> grid, Position position) {
    return isPositionWithinBounds(grid, position)
        ? Optional.of(grid.get(position))
        : Optional.empty();
  }

  private static boolean isPositionWithinBounds(Grid<TileSimulation> grid, Position position) {
    int x = position.x();
    int y = position.y();
    return x >= 0 && x < grid.getWidth() && y >= 0 && y < grid.getHeight();
  }
}
