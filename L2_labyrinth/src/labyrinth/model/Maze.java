package labyrinth.model;

import labyrinth.service.serialization.MazeDTO;
import labyrinth.service.serialization.TileDTO;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableValue;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Maze {
    private final Set<Integer> fixedRows;
    private final Set<Integer> fixedColumns;
    private final Grid<SimpleObservableValue<Tile>> tiles;
    private final SimpleObservableValue<Tile> extraTile;
    private SimpleObservableValue<MazeMove> move; //le move qui vient d'etre fait

    public Maze(Grid<Tile> tiles, Tile extraTile, Set<Integer> fixedRows, Set<Integer> fixedColumns) {
        //verification de null
        if (tiles == null || extraTile == null || fixedRows == null || fixedColumns == null){
            throw new NullPointerException();
        }
        //verifier tuiles null
        for(Grid.Entry<Tile> entry : tiles){
            if(entry.value() == null){
                throw new NullPointerException();
            }
        }
        //verifions les doublons de tuiles
        Set<Tile> seen = new HashSet<Tile>();
        for(Grid.Entry<Tile> entry : tiles){
            if(!seen.add(entry.value())){
                throw new IllegalArgumentException();
            }
        }
        //Vérifions que chaque trésor n'est présent qu'une seule fois
        int [] compteursTresors = new int[24];
        for(Grid.Entry<Tile> entry : tiles){ //pour chaque tuile de la grille
            Content c = entry.value().getContent();  //on récupère le contenu
             if(c instanceof Treasure(int index)){     // si c'est un trésor
                 //je récupère l'id
                 if(index < 0 || index >=24){
                     throw new IllegalArgumentException("Trésor avec id invalide : " + index);
                 }
                 compteursTresors[index] += 1;      //et j'augmente son nombre d'occurrences
             }
        }
        // vérifier aussi la tuile extra : elle fait partie du labyrinthe et peut contenir un trésor
        if(extraTile.getContent() instanceof Treasure treasureExtra){
            int index = treasureExtra.id();
            if(index < 0 || index >= 24){
                throw new IllegalArgumentException("Trésor avec id invalide : " + index);
            }
            compteursTresors[index] += 1;
        }
        for(int i = 0 ; i < 24 ; i++){
            if(compteursTresors[i]==0){              // si un trésor à 0 occurence
                throw  new IllegalArgumentException("Le trésor "+ i+ " est absent du labyrinthe");
            }
            if(compteursTresors[i] > 1){                    // si plus d'une occurence
                throw  new IllegalArgumentException("Le trésor "+ i +" apparaît plus d'une fois dans le labyrinthe");
            }
        }
        this.fixedRows = fixedRows;
        this.fixedColumns = fixedColumns;
        this.tiles = tiles.map(SimpleObservableValue::new);   //on ne passe pas la référence mais une copie
        this.extraTile = new SimpleObservableValue<>(extraTile);
    }

    public Grid<ObservableValue<Tile>> getObservableTiles(){
        return tiles.map(SimpleObservableValue::asReadOnly);
    }

    public Grid<Tile> getTiles() {
        return tiles.map(SimpleObservableValue::getValue);  //pour éviter de modifier tiles
    }

    public Position getPosition(Tile tile) {
        for (Grid.Entry<SimpleObservableValue<Tile>> entry : tiles) {
            if (tile.equals(entry.value().getValue())) {
                return entry.position();
            }
        }
        return null;
    }

    public Set<Integer> getFixedRows() {
        return this.fixedRows;
    }

    public Set<Integer> getFixedColumns() {
        return this.fixedColumns;
    }

    public Tile getExtraTile() {
        return this.extraTile.getValue();
    }

    public ObservableValue<Tile> getExtraTileProperty() {
        return this.extraTile.asReadOnly();
    }

    public Tile getTile(Player player) {
        for (Grid.Entry<SimpleObservableValue<Tile>> entry : tiles) {
            for (Player playertmp : entry.value().getValue().getPlayers()) {
                if (playertmp.equals(player)) {
                    return entry.value().getValue();
                }
            }
        }
        return null;
    }

    public Tile getStarting(Player player) {
        int index = player.getIndex();
        int width = getTiles().getWidth();
        int height = getTiles().getHeight();
        return switch (index) {
            case 0 -> getTiles().get(0, 0);
            case 1 -> getTiles().get(width - 1, 0);
            case 2 -> getTiles().get(width - 1, height - 1);
            case 3 -> getTiles().get(0, height - 1);
            default -> throw new IllegalArgumentException("Invalide index");
        };
    }

    public MazeMove getForbiddenMove() {
        if (move == null) {
            return null;
        }
        return move.getValue().getOpposite();
    }

    public ObservableValue<MazeMove> getForbiddenMoveProperty() {
        if (move == null) {
            return null;
        }
        return move.asReadOnly();
    }

    public void move(MazeMove move) {
        //Vérifions les interdits
        if (move == null) { //on refuse un move null
            throw new IllegalArgumentException("Move ne peut pas etre null");
        }
        //on interdit l'opposé d'un mouvement qui vient d'etre joué
        //on ne peut pas pousser la tuile dans l'autre sens
        if (getForbiddenMove() != null && getForbiddenMove().equals(move)) {
            throw new IllegalArgumentException("Move interdit");
        }
        //si l'index est hors de la grille
        if(move.index() < 0 || move.index() >= getTiles().getWidth()){
            throw new IllegalArgumentException("Index hors grille");
        }
        Tile sortante; //stocker la tuile qui sort
        // tuile entrante = l'ancienne tuile extra, qui va entrer dans le labyrinthe
        Tile entrant = extraTile.getValue();
        switch (move.direction()){ //choix selon la direction
            //insertion par le bas(pousser vers le haut)
            case UP:
                //la tuile sortante est completement à haut
                sortante = tiles.get(move.index(), 0).getValue();
                //decalage vers le haut
                for(int i = 0; i < getTiles().getHeight() - 1; i++) {
                    tiles.get(move.index(), i).setValue(tiles.get(move.index(), i + 1).getValue());
                }
                tiles.get(move.index(), getTiles().getHeight() - 1).setValue(extraTile.getValue());
                extraTile.setValue(sortante);
                break;
            //insertion par le haut(pousser vers le bas)
            case DOWN:
                //la tuile sortante est completement à bas
                sortante = tiles.get(move.index(), getTiles().getHeight() - 1).getValue();
                //decalage vers le bas
                for(int i = getTiles().getHeight() - 1; i > 0; i--) {
                    tiles.get(move.index(), i).setValue(tiles.get(move.index(), i - 1).getValue());
                }
                tiles.get(move.index(), 0).setValue(extraTile.getValue());
                extraTile.setValue(sortante);
                break;
            //insertion depuis la droite (pousser vers la gauche)
            case LEFT:
                //la tuile sortante est completement à gauche
                sortante = tiles.get(0, move.index()).getValue();
                //on decale toutes les tuiles vers la gauche
                for(int i = 0; i < getTiles().getWidth() - 1; i++) {
                    tiles.get(i, move.index()).setValue(tiles.get(i + 1, move.index()).getValue());
                }
                tiles.get(getTiles().getWidth() - 1, move.index()).setValue(extraTile.getValue());
                extraTile.setValue(sortante);
                break;
            //insertion depuis la gauche (pousser vers la droite)
            case RIGHT:
                //la tuile sortante est completement à droite
                sortante = tiles.get(getTiles().getWidth() - 1, move.index()).getValue();
                for(int i = getTiles().getWidth() - 1; i > 0; i--) {
                    tiles.get(i, move.index()).setValue(tiles.get(i - 1, move.index()).getValue());
                }
                tiles.get(0, move.index()).setValue(extraTile.getValue());
                extraTile.setValue(sortante);
                break;
            default:
                sortante = null;
        }

        if (sortante != null && !sortante.getPlayers().isEmpty()) {
            List<Player> joueursADeplacer = new java.util.ArrayList<>(sortante.getPlayers());
            for (Player p : joueursADeplacer) {
                sortante.removePlayer(p);
                entrant.addPlayer(p);
            }
        }
        //on mémorise le mouvement effectué
        this.move = new SimpleObservableValue<>(move);
    }


    public static Maze from(MazeDTO mazeDTO, List<Player> players) {
        if (mazeDTO == null) {
            throw new IllegalArgumentException("MazeDTO null");
        }
        int w = mazeDTO.getTiles().getWidth();
        int h = mazeDTO.getTiles().getHeight();

        // on recrée la grille de tuiles
        Grid<Tile> grid = new ArrayGrid<>(w, h);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                TileDTO tileDTO = mazeDTO.getTiles().get(i, j);
                Tile tile = Tile.from(tileDTO, players);
                grid.set(i, j, tile);
            }
        }

        // tuile extra
        Tile extra = Tile.from(mazeDTO.getExtraTile(), players);
        // les lignes et colonnes fixes
        Set<Integer> fixedRows = new HashSet<>(mazeDTO.getFixedRows());
        Set<Integer> fixedColumns = new HashSet<>(mazeDTO.getFixedColumns());

        return new Maze(grid, extra, fixedRows, fixedColumns);
    }
}

