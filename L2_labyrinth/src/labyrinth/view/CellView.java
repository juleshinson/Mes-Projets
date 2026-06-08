package labyrinth.view;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import labyrinth.viewmodel.CellViewModel;

// StackPane pour permettre l'affichage des joueurs sur les tuiles
public class CellView extends StackPane {


    public CellView(CellViewModel cellViewModel) {
        // l'image de la tuile en fond
        TileView tileView = new TileView(cellViewModel.getTileViewModel());
        tileView.setFitWidth(50);
        tileView.setFitHeight(50);

        // image du joueur présent sur la tuile (invisible par défaut)
        ImageView joueurView = new ImageView();
        joueurView.setFitWidth(22);
        joueurView.setFitHeight(22);
        joueurView.setPreserveRatio(true);
        joueurView.setMouseTransparent(true);
        StackPane.setAlignment(joueurView, Pos.TOP_LEFT);

        // lettre du trésor (A=1, B=2, …) au centre de la tuile
        Text treasureLabel = new Text();
        treasureLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        treasureLabel.setFill(Color.GOLDENROD);
        treasureLabel.setMouseTransparent(true);
        StackPane.setAlignment(treasureLabel, Pos.CENTER);

        int treasureInitial = cellViewModel.getTileViewModel().treasureProperty().getValue();
        treasureLabel.setText(treasureInitial >= 0 ? String.valueOf((char)('A' + treasureInitial)) : "");

        cellViewModel.getTileViewModel().treasureProperty().addListener((obs, oldV, newV) -> {
            int id = newV;
            treasureLabel.setText(id >= 0 ? String.valueOf((char)('A' + id)) : "");
        });

        // initialiser l'image avec la valeur courante (joueur déjà présent à la construction de CellView)
        int idxInitial = cellViewModel.getTileViewModel().currentPlayerProperty().getValue();
        if (idxInitial >= 0) {
            joueurView.setImage(new Image("/player" + (idxInitial + 1) + ".png", 22, 22, true, true));
        }
        // quand le joueur présent change, on met à jour l'image
        cellViewModel.getTileViewModel().currentPlayerProperty().addListener((obs, oldV, newV) -> {
            int idx = newV;
            if (idx >= 0) {
                joueurView.setImage(new Image("/player" + (idx + 1) + ".png", 22, 22, true, true));
            } else {
                joueurView.setImage(null);
            }
        });

        this.getChildren().addAll(tileView, treasureLabel, joueurView);
        this.setPrefSize(50, 50);
        this.setMaxSize(50, 50);
        this.setMinSize(50, 50);

        DropShadow dropShadow = new DropShadow();

        cellViewModel.activeProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                dropShadow.setRadius(10);
                dropShadow.setSpread(0.5);
                this.setEffect(dropShadow);
            } else {
                this.setEffect(null);
            }
        });

        this.setOnMouseEntered(e -> {
            if (cellViewModel.activeProperty().getValue()) {
                this.setEffect(dropShadow);
            }
        });

        this.setOnMouseExited(e -> {
            if (cellViewModel.activeProperty().getValue()) {
                this.setEffect(dropShadow);
            } else {
                this.setEffect(null);
            }
        });

        this.setOnMouseClicked(e -> {
            if (cellViewModel.activeProperty().getValue()) {
                cellViewModel.movePlayer();
            }
        });
    }
}