package labyrinth.view;


import javafx.scene.image.ImageView;
import labyrinth.ImageManager;
import labyrinth.viewmodel.TileViewModel;

public class TileView extends ImageView {
    private final  TileViewModel tileViewModel;

    public TileView(TileViewModel tileViewModel) {

        this.tileViewModel = tileViewModel;
        //un premier appel à assignImage pour initialiser
        assignImage();
        //on écoute la direction,le kind,la starting position, le treasureproperty pour mettre à jour
        tileViewModel.directionProperty().addListener((obs, oldV, newV) -> assignImage());
        tileViewModel.kindProperty().addListener((obs, oldV, newV) -> assignImage());
        tileViewModel.startingPositionProperty().addListener((obs,oldV,newV)->assignImage());
        tileViewModel.treasureProperty().addListener((obs,oldV,newV)->assignImage());

        //on ajuste la taille de l'image
        this.setFitWidth(50);
        this.setFitHeight(50);
        this.setPreserveRatio(false);
    }

    public void assignImage() {
            this.imageProperty().set(ImageManager.getInstance().getTileImage(
                    tileViewModel.getKind(),
                    tileViewModel.getDirection())
            );
        }
    }





