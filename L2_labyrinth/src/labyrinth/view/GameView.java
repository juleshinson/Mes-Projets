package labyrinth.view;


import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import labyrinth.model.GameState;
import labyrinth.viewmodel.GameViewModel;


public class GameView extends BorderPane {

    public GameView(GameViewModel model){
        //On crée les différentes vues du game
        MenuView menuView = new MenuView(model) ;
        MainView mainView = new MainView(model);

        // On crée et on initialise les players
        PlayersView playersView = new PlayersView(model.getPlayersViewModel(), model.getUserProfilesViewModel());

        // panneau d'info côté gauche : affiche le joueur courant et la phase sans bloquer la vue
        Label tourLabel = new Label();
        tourLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 8; -fx-text-fill: #333;");
        tourLabel.setWrapText(true);
        tourLabel.setMaxWidth(100);

        VBox sidePanel = new VBox(tourLabel);
        sidePanel.setStyle("-fx-background-color: #e8f4e8; -fx-border-color: #aaa; -fx-border-width: 0 1 0 0; -fx-padding: 10 6;");
        sidePanel.setPrefWidth(110);

        // met à jour le label côté quand l'état ou le joueur change
        Runnable updateTourLabel = () -> {
            GameState etat = model.getState();
            if (etat == null || etat == GameState.SETUP) {
                tourLabel.setText("En attente\nde joueurs");
            } else if (etat == GameState.GAME_OVER) {
                int j = model.getCurrentPlayer() + 1;
                tourLabel.setText("Joueur " + j + "\na gagné !");
            } else {
                int j = model.getCurrentPlayer() + 1;
                String phase = etat == GameState.MAZE_MOVE ? "Phase 1\n(labyrinthe)"
                             : etat == GameState.PLAYER_MOVE ? "Phase 2\n(déplacement)"
                             : "";
                tourLabel.setText("Tour :\nJoueur " + j + "\n\n" + phase);
            }
        };
        model.stateProperty().addListener((obs, oldV, newV) -> updateTourLabel.run());
        model.currentPlayerProperty().addListener((obs, oldV, newV) -> updateTourLabel.run());
        updateTourLabel.run();

        //on les dispose dans la fenêtre
        this.setLeft(sidePanel);
        this.setRight(playersView);
        this.setCenter(mainView);
        this.setTop(menuView);

    }
}
