package labyrinth.model;


import labyrinth.service.serialization.TileDTO;
import labyrinth.util.observable.ObservableList;
import labyrinth.util.observable.ObservableValue;
import labyrinth.util.observable.SimpleObservableList;
import labyrinth.util.observable.SimpleObservableValue;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tile {
    private final SimpleObservableValue<Direction> orientation ;
    private final TileKind kind ;
    private SimpleObservableValue<Content> content;
    private SimpleObservableList<Player> players ;

    public Tile(TileKind kind){
        this(kind , new None(), Direction.UP) ;
    }

    public Tile(TileKind kind, Content content){
        this(kind, content, Direction.UP) ;
    }

    public Tile(TileKind kind, Content content, Direction orientation){
        this.kind = kind ;
        this.content = new SimpleObservableValue<>(content) ;
        this.orientation = new SimpleObservableValue<>(orientation) ;
        this.players = new SimpleObservableList<>() ;
    }

    public TileKind getKind(){
        return kind ;
    }

    public Content getContent(){
        return content.getValue() ;
    }

    public ObservableValue<Content> contentProperty(){
        return content.asReadOnly() ;
    }

    public void setContent(Content content){
        this.content.setValue(content) ;
    }
    public ObservableValue<Direction> orientationProperty(){
        return orientation.asReadOnly() ;
    }

    public Direction getOrientation(){
        return orientation.getValue() ;
    }

    public Set<Direction> getExits() {
        Set<Direction> baseExits = getKind().getExits();
        Set<Direction> rotatedExits = new HashSet<>();

        for(Direction exit : baseExits) {
            rotatedExits.add(exit.getRotated(getOrientation()));
        }
        return rotatedExits;
    }

    public ObservableList<Player> playersProperty() {
        return players.asReadOnly();
    }

    public List<Player> getPlayers(){
        return players ;
    }

    public void addPlayer(Player player){
        if(!hasPlayer(player.getIndex()))getPlayers().add(player) ;
    }

    public boolean hasPlayer(int index){
        for(Player player : getPlayers()){
            if(player.getIndex() == index){
                return true;
            }
        }
        return false;
    }

    public void removePlayer(Player player){if(hasPlayer(player.getIndex()))getPlayers().remove(player);
    }

    public void rotate(int nbTimes){
        // getOrientation().getRotated(nbTimes); // ancien code : calculait la direction mais ne la stockait pas → aucun effet
        orientation.setValue(getOrientation().getRotated(nbTimes));
    }

    // méthode statique pour recréer une tuile depuis un DTO
    // on a besoin de la liste des joueurs pour retrouver les instances
    public static Tile from(TileDTO tileDTO, List<Player> players) {
        Tile tile = new Tile(tileDTO.getTileKind(), tileDTO.getContent(), tileDTO.getOrientation());
        // on remet les joueurs sur la tuile
        for (int indexJoueur : tileDTO.getPlayers()) {
            // on cherche le joueur qui a cet index dans la liste
            for (Player p : players) {
                if (p != null && p.getIndex() == indexJoueur) {
                    tile.addPlayer(p);
                    break; // trouvé, on sort
                }
            }
        }
        return tile;
    }
}
