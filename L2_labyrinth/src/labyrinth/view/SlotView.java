package labyrinth.view;

import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import labyrinth.ImageManager;
import labyrinth.viewmodel.SlotViewModel;
import labyrinth.viewmodel.TileViewModel;


public class SlotView extends Pane {
    private final TileView tileView;
    private final ImageView overlay = new ImageView();

    public SlotView(SlotViewModel slotViewModel, TileViewModel extraTile) {
        //on initialise la tuile extra
        this.tileView = new TileView(extraTile);

        //on la rend invisible pour le moment
        this.tileView.setVisible(false);

        //on la rend transparente à la souris
        tileView.setMouseTransparent(true);

        //on définit les dimensions de l'image de la tuile
        overlay.setFitWidth(50);
        overlay.setFitHeight(50);
        overlay.setPreserveRatio(true);
        getChildren().add(overlay);
        this.setPickOnBounds(true);
        this.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: rgba(0,0,0,0.01);");

        // si la souris entre on magnetize
        this.setOnMouseEntered(event -> {
            tileView.setMouseTransparent(true);
            slotViewModel.magnetize();
        });
        //si elle sort on démagnétize
        this.setOnMouseExited(event -> {
            tileView.setMouseTransparent(true);
            slotViewModel.demagnetize();
        });

        this.setPrefSize(50, 50);  // on ajuste la taille
        this.getChildren().add(tileView);  //on met la tuile dans le Pane
        this.setStyle(" -fx-border-color: black; -fx-border-width: 1;"); //on met un style léger
        Glow glow = new Glow(10.5);     //un effet glow

        //on met un écouteur pour écouter magnetizedProperty() pour rendre la tuile visible et transparente à la souris si nécessaire
        slotViewModel.magnetizedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                this.tileView.setVisible(true);    //on rend la tuile visible
                this.tileView.setMouseTransparent(false);  //et sensible à la souris
                this.setEffect(glow);       //on met l'effet

            } else {
                this.tileView.setVisible(false);
                this.tileView.setMouseTransparent(true);
                this.setEffect(null);  //on enlève l'effet
            }
        });

        // moveMaze() déclenché au clic sur le slot
        this.setOnMouseClicked(event -> slotViewModel.moveMaze());

       //on ajoute un écouteur pour mettre l'image forbidden dans le background si necessaire sinon on l'enlève
        slotViewModel.forbiddenProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                Image image = new Image("/forbidden.png");
                BackgroundImage bgImage = new BackgroundImage(
                        image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, false)
                );
                setBackground(new Background(bgImage));

            } else {
                setBackground(null);
                setStyle("-fx-border-color: black; -fx-border-width: 1;");
            }
        });

        // on écoute activeproperty  pour mettre à jour overlay
        slotViewModel.activeProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                Image image = ImageManager.getInstance().getTileImage(
                        extraTile.getKind(),
                        extraTile.getDirection()
                );
                overlay.setImage(image);
            } else {
                overlay.setImage(null);
            }
        });


    }
}




