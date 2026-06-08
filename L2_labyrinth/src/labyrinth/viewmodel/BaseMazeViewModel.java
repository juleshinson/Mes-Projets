package labyrinth.viewmodel;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import labyrinth.model.*;
import labyrinth.util.adapter.FxAdapter;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.MazeToGraphAdapter;
import labyrinth.util.graph.Paths;

import java.util.*;

public class BaseMazeViewModel implements  MazeViewModel{
    private Grid<CellViewModel> cellViewModel; //à vérifier
    private final Map<MazeMove, SlotViewModel> mazeMoveSlotViewModel;
    private CellViewModel extraTile;
    private final ObjectProperty<MazeMove> forbiddenMove = new SimpleObjectProperty<>(new MazeMove(Direction.UP,1));
    private final ObjectProperty<GameState> stateProperty = new SimpleObjectProperty<>();
    private final Game game;

    public BaseMazeViewModel(Game game) {
        this.game = game;
        // grille temporaire vide sera remplacée quand le jeu démarre
        this.cellViewModel = new ArrayGrid<>(7, 7);
        this.mazeMoveSlotViewModel = new LinkedHashMap<>();
        this.extraTile = new BaseCellViewModel(new Position(0,0));
        for (int r = 0; r < cellViewModel.getWidth(); r++) {

            for (int c = 0; c < cellViewModel.getHeight(); c++) {

                cellViewModel.set(r, c, new BaseCellViewModel(new Position(r,c)));
            }
        }
        this.randomize();

        int width = 7;
        int height = 7;

        // TOP → push DOWN
        for (int j = 0; j < width; j++) {
            MazeMove move = new MazeMove(Direction.DOWN, j);
            this.mazeMoveSlotViewModel.put(move, new BaseSlotViewModel(move, this));
        }

        // BOTTOM → push UP
        for (int j = 0; j < width; j++) {
            MazeMove move = new MazeMove(Direction.UP, j);
            this.mazeMoveSlotViewModel.put(move, new BaseSlotViewModel(move, this));
        }

        // LEFT → push RIGHT
        for (int i = 0; i < height; i++) {
            MazeMove move = new MazeMove(Direction.RIGHT, i);
            this.mazeMoveSlotViewModel.put(move, new BaseSlotViewModel(move, this));
        }

        // RIGHT → push LEFT
        for (int i = 0; i < height; i++) {
            MazeMove move = new MazeMove(Direction.LEFT, i);
            this.mazeMoveSlotViewModel.put(move, new BaseSlotViewModel(move, this));
        }
        // quand le game démarre, on reconstruit la grille depuis le vrai labyrinthe
        game.stateProperty().addListener((obs, oldV, newV) -> {
            if (newV == GameState.MAZE_MOVE && oldV == GameState.SETUP) {
                // premiere fois qu'on passe en MAZE_MOVE : le labyrinthe vient d'etre créé
                initDepuisGame();
            }
            if (newV == GameState.PLAYER_MOVE) {
                calculerCellulesActives();
            }
            if (newV == GameState.MAZE_MOVE) {
                desactiverTout();
            }
            // quand le tour se termine on appelle endTurn automatiquement
            // Platform.runLater pour éviter les appels récursifs quand les bots jouent
            if (newV == GameState.TURN_END) {
                Platform.runLater(() -> {
                    try {
                        game.endTurn() ;
                    }catch (Exception e) {
                        System.out.println("endTurn erreur : " + e.getMessage()) ;
                    }
                });
            }
            stateProperty.setValue(newV);
        });
    }

    private void calculerCellulesActives() {
        if (game.getMaze() == null || game.getCurrentPlayer() == null) {
            return;
        }
        Tile tuileCourante = game.getMaze().getTile(game.getCurrentPlayer()) ;
        if (tuileCourante == null) {
            return;
        }
        Paths<Tile> chemins = new Paths<>(MazeToGraphAdapter.getGraph(game.getMaze()), tuileCourante) ;

        for (int i = 0; i < cellViewModel.getWidth(); i++) {
            for (int j = 0; j < cellViewModel.getHeight(); j++) {
                BaseCellViewModel cell = (BaseCellViewModel) cellViewModel.get(i, j) ;
                Tile tile = game.getMaze().getTiles().get(i, j) ;
                cell.setActive(chemins.isAccessible(tile)) ;
            }
        }
    }

    private void desactiverTout() {
        for (int i = 0; i < cellViewModel.getWidth(); i++) {
            for (int j = 0; j < cellViewModel.getHeight(); j++) {
                ((BaseCellViewModel) cellViewModel.get(i, j)).setActive(false) ;
            }
        }
    }

    // reconstruit la grille depuis le vrai labyrinthe
    private void initDepuisGame() {
        Maze maze = game.getMaze();
        if (maze == null) {
            return;
        }
        int w = maze.getTiles().getWidth();
        int h = maze.getTiles().getHeight();
        cellViewModel = new ArrayGrid<>(w, h);
        // getObservableTiles() renvoie Grid<ObservableValue<Tile>> du cours
        // FxAdapter.adapt() le convertit en javafx.beans.value.ObservableValue<Tile>
        Grid<labyrinth.util.observable.ObservableValue<Tile>> observableTiles = maze.getObservableTiles();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                javafx.beans.value.ObservableValue<Tile> fxObs = FxAdapter.adapt(observableTiles.get(i, j));
                cellViewModel.set(i, j, new BaseCellViewModel(new Position(i, j), fxObs, game));
            }
        }
        // tuile extra
        ObservableValue<Tile> extraObs = FxAdapter.adapt(maze.getExtraTileProperty());
        extraTile = new BaseCellViewModel(new Position(0, 0), extraObs, game);
    }

    public Grid<CellViewModel> getCellViewModels() {

        return this.cellViewModel;
    }

    public TileViewModel getExtraTileViewModel() {
        return extraTile.getTileViewModel();
    }

    public void rotateExtraTile(int nbTimes) {
        if (game.getState() == GameState.MAZE_MOVE) {
            game.rotateExtraTile(nbTimes);
            // le listener dans BaseCellViewModel met déjà à jour le viewmodel automatiquement
            // donc on ne rappelle pas getExtraTileViewModel().rotate() ici
        }
    }

    public Map<MazeMove, SlotViewModel> getSlotViewModels() {
        return Collections.unmodifiableMap(this.mazeMoveSlotViewModel);
    }


    public void randomize() {
        Grid<CellViewModel> grid = getCellViewModels();
        for (int row = 0; row < grid.getWidth(); row++) {
            for (int col = 0; col < grid.getHeight(); col++) {
                CellViewModel cell = grid.get(row, col);
                BaseTileViewModel tile = (BaseTileViewModel) cell.getTileViewModel();

                TileKind[] tileKinds = TileKind.values();
                tile.setKind(tileKinds[(int) (Math.random() * tileKinds.length)]);

                Direction[] directions = Direction.values();
                tile.setDirection(directions[(int) (Math.random() * directions.length)]);
            }
        }

    }

    public void rotateAll(int nbTimes) {
        for (int i = 0; i < this.cellViewModel.getWidth(); i++) {
            for (int j = 0; j < this.cellViewModel.getHeight(); j++) {
                CellViewModel cellViewModel = this.cellViewModel.get(i, j);
                cellViewModel.getTileViewModel().getDirection().getRotated(nbTimes);

            }
        }
        getExtraTileViewModel().getDirection().getRotated(nbTimes);
    }

    public MazeMove getForbiddenMove() {
        return forbiddenMove.get();
    }

    public void setForbiddenMove(MazeMove move) {
        forbiddenMove.set(move);
    }


    public ObservableValue<MazeMove> forbiddenMoveProperty() {
        return forbiddenMove;
    }

    private static List<MazeMove> getMazeMoves(int width, int height) {
        List<MazeMove> moves = new ArrayList<>();

// TOP
        for (int j = 0; j < width; j++) {
            moves.add(new MazeMove(Direction.DOWN, j));
        }

// BOTTOM
        for (int j = 0; j < width; j++) {
            moves.add(new MazeMove(Direction.UP, j));
        }
// LEFT (sans les coins)
        for (int k = 0; k < height; k++)
            moves.add(new MazeMove(Direction.RIGHT, k));

// RIGHT (sans les coins)
        for (int k = 0; k < height; k++)
            moves.add(new MazeMove(Direction.LEFT, k));
        return moves;
    }

    public void moveMaze(MazeMove move) {
        if(game.getState() != GameState.MAZE_MOVE) {
            return;
        }
        setForbiddenMove(move.getOpposite());  // mettre avant pour que MazeView ait la bonne info
        try {
            game.moveMaze(move);
        } catch (Exception e) {
            System.out.println("moveMaze erreur : " + e.getMessage());
        }
    }


    public ObservableValue<GameState> stateProperty() {
        return stateProperty;
    }


    public GameState getState() {
        return stateProperty.getValue();
    }

    public void setState(GameState gameState){
        stateProperty.setValue(gameState);
    }
}
