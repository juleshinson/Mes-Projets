package labyrinth.service.serialization;

import labyrinth.model.*;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;

import java.util.ArrayList;
import java.util.List;

public class MazeDTO {
    private final List<Integer> fixedRows ;
    private final List<Integer>fixedColumns ;
    private final Grid<TileDTO> tiles ;
    private final TileDTO extraTile ;
    private final MazeMove forbiddenMove;
    public MazeDTO(Maze maze) {
        this.fixedRows = new ArrayList<>(maze.getFixedRows());
        this.fixedColumns = new ArrayList<>(maze.getFixedColumns());
        // ici on initialise la grille avec les bonnes dimensions sinon NullPointerException
        int w = maze.getTiles().getWidth();
        int h = maze.getTiles().getHeight();
        this.tiles = new ArrayGrid<>(w, h);
        for(Grid.Entry<Tile> entry : maze.getTiles()) { //pour chaque tuile
            Tile tile = entry.value();  //on récupère le contenu
            this.tiles.set(entry.position(),new TileDTO(tile));

        }
        this.extraTile = new TileDTO(maze.getExtraTile()) ;
        this.forbiddenMove = maze.getForbiddenMove() ;
    }

    public Grid<TileDTO> getTiles(){
        return tiles ;
    }

    public List<Integer> getFixedRows(){
        return fixedRows ;
    }

    public List<Integer> getFixedColumns(){
        return fixedColumns ;
    }

    public TileDTO getExtraTile(){
        return extraTile ;
    }

    public MazeMove getForbiddenMove(){
        return forbiddenMove ;
    }


}
