package labyrinth.view;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import labyrinth.model.MazeMove;
import labyrinth.viewmodel.MazeViewModel;
import labyrinth.viewmodel.SlotViewModel;

import static labyrinth.model.GameState.*;

public class MazeView extends StackPane {
    public TileView extraTileView ;

    public MazeView(MazeViewModel mazeViewModel) {


        //Initialisation
        BoardView boardView = new BoardView(mazeViewModel);
        this.extraTileView = new TileView(mazeViewModel.getExtraTileViewModel());

        //Taille de extraTileView
        extraTileView.setFitWidth(50);
        extraTileView.setFitHeight(50);

        //Position de extraTileView
        boardView.boundsInParentProperty().addListener((obs, oldB, newB) -> {
            extraTileView.setLayoutX(newB.getMinX());
            extraTileView.setLayoutY(newB.getMaxY());
        });


        Pane composant1 = new Pane(boardView,extraTileView);  //On crée un composant pour boardview et l'extraTileView
        // centrage : USE_PREF_SIZE empêche composant1 de remplir tout le StackPane StackPane le centre automatiquement
        composant1.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        mazeViewModel.stateProperty().addListener((obs,oldV,newV)->{
            if(newV.equals(PLAYER_MOVE)){
                MazeMove forbiddenMove = mazeViewModel.getForbiddenMove();
                SlotViewModel slotViewModel = mazeViewModel.getSlotViewModels().get(forbiddenMove);
                if(slotViewModel != null){
                    slotViewModel.setActive(true);
                }
            }
            if(newV.equals(MAZE_MOVE)){
                for (SlotViewModel slot : mazeViewModel.getSlotViewModels().values()) {
                    slot.setActive(false);
                }
                setBackground(null);
                setStyle("-fx-border-color: black; -fx-border-width: 1;");
                // focus automatique à chaque début de tour pour que A/E fonctionnent sans clic préalable
                requestFocus();
            }
        });
        extraTileView.setStyle("-fx-background-color: red;");
        TileView tuileFlottante = new TileView(mazeViewModel.getExtraTileViewModel());

        tuileFlottante.setVisible(false);
        Pane composant2 = new Pane(tuileFlottante);
        composant2.setPickOnBounds(false);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(composant2.widthProperty());
        clip.heightProperty().bind(composant2.widthProperty());
        composant2.setClip(clip);

        //Position de la tuile fixe
        StackPane.setAlignment(composant2, Pos.TOP_LEFT);
        composant2.prefWidthProperty().bind(boardView.widthProperty());
        composant2.prefHeightProperty().bind(boardView.heightProperty());


        composant2.translateXProperty().bind(composant1.layoutXProperty().add(boardView.layoutXProperty()));
        composant2.translateYProperty().bind(composant1.layoutYProperty().add(boardView.layoutYProperty()));

        composant2.setMouseTransparent(true);

        this.getChildren().addAll(composant1,composant2);

        this.setFocusTraversable(true);
        // on déplace les handlers sur this pour que la tuile flottante suive la souris
        this.setOnMouseEntered(e -> {
            requestFocus();
            tuileFlottante.setVisible(true);
        });
        this.setOnMouseMoved(e -> {
            // sceneToLocal pour convertir les coordonnées de MazeView vers composant2
            javafx.geometry.Point2D p = composant2.sceneToLocal(e.getSceneX(), e.getSceneY());
            tuileFlottante.relocate(p.getX() - tuileFlottante.getFitWidth() / 2,
                                   p.getY() - tuileFlottante.getFitHeight() / 2);
        });
        this.setOnMouseExited(e -> tuileFlottante.setVisible(false));

        //Rotation avec les touches A et E
        this.setOnKeyPressed(event->{
            switch (event.getCode()){
                case A :
                    mazeViewModel.rotateExtraTile(1) ;
                    break;
                case E :
                     mazeViewModel.rotateExtraTile(3);
                    break ;
            }
        });

        // Rotation avec le curseur de la souris
        this.setOnScroll(event ->{
            if(event.getDeltaY() > 0){
                mazeViewModel.rotateExtraTile(2);
            }else{
                mazeViewModel.rotateExtraTile(5);
            }
        });



    }

}
