package labyrinth.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import labyrinth.model.GameState;
import labyrinth.viewmodel.GameViewModel;
import labyrinth.viewmodel.MazeViewModel;

public class MainView extends BorderPane {
    private MazeView mazeView ;
    private final GameViewModel gameViewModel;
    private final Label guideLabel = new Label();

    public MainView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
        guideLabel.setStyle("-fx-font-size: 12px; -fx-padding: 6 10; -fx-background-color: #f0f0f0;");
        setBottom(guideLabel);

        //on écoute l'état et on appelle updateView pour mettre à jour la fenêtre
        gameViewModel.stateProperty().addListener((obs, oldV, newV) -> this.updateView(newV));
        updateView(gameViewModel.getState());
    }

    public void updateView(GameState state){
        if(state.equals(GameState.SETUP)){    //au setup pas de labyrinth juste un bouton start
            Button start = new Button("Start");
            start.setOnAction(event-> gameViewModel.startGame());
            setCenter(start);
            guideLabel.setText("Ajoutez des joueurs à droite, puis cliquez sur Start.");
        } else if(state.equals(GameState.MAZE_MOVE)){     //si c'est le premier mazemove on le crée
            if(mazeView == null) {
                MazeViewModel mazeViewModel = gameViewModel.getMazeViewModel();
                mazeView = new MazeView(mazeViewModel);
            }
            setCenter(mazeView);
            int joueur = gameViewModel.getCurrentPlayer() + 1;
            guideLabel.setText("Joueur " + joueur + " — Phase 1 : Insérez une tuile (cliquez une flèche sur le bord). [A] / [E] ou molette pour tourner.");
        } else if(state.equals(GameState.PLAYER_MOVE)){
            int joueur = gameViewModel.getCurrentPlayer() + 1;
            guideLabel.setText("Joueur " + joueur + " — Phase 2 : Déplacez votre pion (cliquez une case mise en surbrillance).");
        } else if(state.equals(GameState.GAME_OVER)){
            int joueur = gameViewModel.getCurrentPlayer() + 1;
            guideLabel.setText("Joueur " + joueur + " a gagné ! Partie terminée.");
        }
    }
}


