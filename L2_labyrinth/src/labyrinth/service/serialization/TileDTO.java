package labyrinth.service.serialization;

import labyrinth.model.*;

import java.util.ArrayList;
import java.util.List;

public class TileDTO {
    private final List<Integer> players ;
    private final TileKind tileKind ;
    private final Content content ;
    private final Direction orientation ;

    public TileDTO(Tile tile){
        this.players = new ArrayList<>() ;
        for(Player player : tile.getPlayers()){
            if(player != null){
                this.players.add(player.getIndex());
            }
        }
        this.tileKind = tile.getKind() ;
        //garder le contenu réel au lieu de le mettre à null
        this.content = tile.getContent();
        this.orientation = tile.getOrientation() ;

    }

    public TileKind getTileKind(){
        return tileKind ;
    }

    public Content getContent(){
        return content ;
    }

    public Direction getOrientation(){
        return orientation ;
    }

    public List<Integer> getPlayers(){
        return players ;
    }
}
