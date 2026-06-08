package labyrinth.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TileKind {
    CORRIDOR(Direction.UP, Direction.DOWN),
    ELBOW(Direction.UP, Direction.RIGHT),
    T_CROSS(Direction.UP, Direction.LEFT, Direction.RIGHT), ;

    private final Set<Direction> exits = new HashSet<>();

    TileKind(Direction...exits){
        this.exits.addAll(Arrays.asList(exits));
    }
    public static TileKind random() {
        TileKind[] values = TileKind.values();
        return values[(int)(Math.random() * values.length)];
    }

    public Set<Direction> getExits(){
        return Set.copyOf(this.exits) ;
    }
}
