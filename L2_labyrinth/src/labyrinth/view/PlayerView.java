package labyrinth.view;


import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import labyrinth.viewmodel.PlayerViewModel;
import labyrinth.viewmodel.UserProfilesViewModel;



public class PlayerView extends VBox {

    public PlayerView(PlayerViewModel playerViewModel, UserProfilesViewModel userProfilesViewModel){
        //on crée 2 HBOX pour positionner les éléments des players
        HBox contentTop = new HBox();
        HBox contentBottom = new HBox();

        Label nameLabel = new Label();
        nameLabel.textProperty().bind(playerViewModel.nameProperty()); // on relie ce label au nom pour l'afficher

        //on propose un mini menu pour ajouter
        AddPlayerButton addButton = new AddPlayerButton(playerViewModel, userProfilesViewModel);
        // on propose un boutton pour retirer le joueur
        Button remove = new Button("-");

        remove.setDisable(!playerViewModel.isActive());

        // au clic on appelle les méthodes correspondantes

        remove.setOnAction(e -> playerViewModel.removePlayer());

        //on crée un rectangle pour simuler deck
        Rectangle deck = new Rectangle(50,50);
        deck.setStyle("-fx-background-color: red");
        Label deckText = new Label("Deck");
        deck.setStroke(Color.BLACK);
        deck.setFill(Color.TRANSPARENT);
        StackPane deckPane = new StackPane(deck,deckText); //le stackpane sert à afficher le deck et son text

        // on créer un rectangle pour simuler discard
        Label discardText = new Label("Discard\n pile");
        Rectangle discard = new Rectangle(60,50);
        discard.setStroke(Color.RED);
        discard.setFill(Color.TRANSPARENT);
        StackPane discardPane = new StackPane(discard,discardText);  // le stackpane sert à afficher le discard et son text

        // on crée aussi un rectangle pour simuler l'extend vue de discard pile
        Rectangle extendDiscard = new Rectangle(100,50);
        Label extendDiscardLabel = new Label("Extended view \nof discard pile");
        extendDiscard.setStroke(Color.RED);
        extendDiscard.setFill(Color.TRANSPARENT);
        StackPane extendDiscardPane = new StackPane(extendDiscard,extendDiscardLabel);

        // on ajoute un listener pour écouter lockedproperty et bloquer les boutons si nécessaire
        playerViewModel.lockedProperty().addListener((observableValue, s, t1) -> {
                addButton.setDisable(playerViewModel.isLocked());
                // remove.setDisable(playerViewModel.isLocked());
                remove.setDisable(playerViewModel.isLocked() || !playerViewModel.isActive());
        });

        // on met à jour le bouton remove selon la présence ou non d'un joueur
        playerViewModel.activeProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean oldV, Boolean newV) -> {
                remove.setDisable(!newV || playerViewModel.isLocked());
        });

        // on crée l'image de l'avatar qui est l'image par défaut
        ImageView avatar = new ImageView(new Image("/default-user.png",50,50,false,false));
        // on ajuste sa taille
        avatar.setFitWidth(50);
        avatar.setFitHeight(50);
        avatar.setPreserveRatio(true);

        // on met en place un listener sur active property pour savoir qu'elle image mettre en fonction de si le joueur existe ou pas
        playerViewModel.activeProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                //getIndex()+1 pour avoir l'image de chaque joueur
                avatar.setImage(new Image("/player"+(playerViewModel.getIndex()+1)+".png",50,50,false,false));
            } else {
                avatar.setImage(new Image("/default-user.png",50,50,false,false));
            }
        });

        // on place les éléments dans leur conteneur
        contentTop.getChildren().addAll(avatar,nameLabel,addButton,remove);
        contentBottom.getChildren().addAll(deckPane,discardPane,extendDiscardPane);

        // on met un spacing pour la lisibilité
        contentTop.setSpacing(10);
        contentBottom.setSpacing(10);


        //on ajoute le tout au VBOX avec du padding et du spacing pour la lisibilité
        getChildren().addAll(contentTop,contentBottom);
        setPadding(new Insets(10));
        setSpacing(5);
    }

    }

