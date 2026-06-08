package labyrinth.util;

import labyrinth.model.*;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.util.*;

public class MazeBuilder {
    private final Grid<Tile> tiles;
    private final List<Tile> freeTiles;
    private final Set<Integer> fixRows;
    private final Set<Integer> fixColumns;

    public MazeBuilder(int width, int height) {
        tiles = new ArrayGrid<>(width, height);
        freeTiles = new ArrayList<>();
        fixRows = new HashSet<>();
        fixColumns = new HashSet<>();
    }

    public MazeBuilder addFixedTile(int x, int y, Tile tile){
        tiles.set(x,y ,tile);
        fixRows.add(x);
        fixColumns.add(y);
        return this;
    }

    public MazeBuilder addFreeTile(Tile tile){
        freeTiles.add(tile);
        return this;
    }

    public Maze build() {

        Iterator<Tile> it = freeTiles.iterator();

        for (int x = 0; x < tiles.getWidth(); x++) {
            for (int y = 0; y < tiles.getHeight(); y++) {
                if (tiles.get(x, y) == null && it.hasNext()) {
                    tiles.set(x, y, it.next());
                }
            }
        }
        Tile extraTile = it.hasNext() ? it.next() : null;
        return new Maze(tiles, extraTile, fixRows, fixColumns);
    }


 public static Maze buildStandardMaze() {

     MazeBuilder builder = new MazeBuilder(7, 7);

     // --- Tuiles fixes officielles ---
     builder.addFixedTile(0, 0, new Tile(TileKind.ELBOW, new StartingPosition(0), Direction.RIGHT));
     builder.addFixedTile(6, 0, new Tile(TileKind.ELBOW, new StartingPosition(1), Direction.DOWN));
     builder.addFixedTile(6, 6, new Tile(TileKind.ELBOW, new StartingPosition(2), Direction.LEFT));
     builder.addFixedTile(0, 6, new Tile(TileKind.ELBOW, new StartingPosition(3), Direction.UP));

     builder.addFixedTile(2, 2, new Tile(TileKind.T_CROSS));
     builder.addFixedTile(2, 4, new Tile(TileKind.T_CROSS));
     builder.addFixedTile(4, 2, new Tile(TileKind.T_CROSS));
     builder.addFixedTile(4, 4, new Tile(TileKind.T_CROSS));

     //  Tuiles libres (41 tuiles)
     for (int i = 0; i < 42; i++) {
         builder.addFreeTile(new Tile(TileKind.random()));
     }

     // Placement des 24 trésors
     List<Tile> free = builder.freeTiles;
     Collections.shuffle(free);

     for (int id = 0; id < 24; id++) {
         free.get(id).setContent(new Treasure(id));
     }

     return builder.build();
 }


}
