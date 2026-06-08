package labyrinth;

import javafx.scene.image.Image;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static final ImageManager INSTANCE = new ImageManager();
    private final Map<String, Image> cache = new HashMap<>();

    private ImageManager(){
    }
    public static ImageManager getInstance(){
        return INSTANCE ;
    }

    public Image getImage(String path){
        if(cache.containsKey(path)){
            return cache.get(path);
        }
        Image image = new Image(path);
        cache.put(path,image);
        return image ;

    }

    public Image getTileImage(TileKind kind, Direction direction){
        int angle = direction.getAngle();
        String path ="" ;
        // Adapter les angles selon le type de tuile
        switch (kind) {
            case CORRIDOR:
                if (angle == 180) angle = 0;
                if (angle == 270) angle = 90;
                path  = "/img/tiles/corridor-"+angle +".png" ;
                break;
            case ELBOW:
                path = "/img/tiles/elbow-" + angle + ".png";
                break;
            case T_CROSS:
                path = "/img/tiles/tcross-" + angle + ".png";
                break;
        }
        return getImage(path);
    }
}
