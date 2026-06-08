package labyrinth;

import labyrinth.model.Game;
import labyrinth.model.GameState;
import labyrinth.service.Services;
import labyrinth.service.serialization.GameDTO;
import labyrinth.util.observable.ChangeListener;
import labyrinth.util.observable.ObservableValue;

// attache des ecouteurs sur le Game et sauvegarde en BDD a la fin de chaque tour
public class SaveService {

    private final Game game;
    private final Services services;

    public SaveService(Game game, Services services) {
        this.game = game;
        this.services = services;

        // on s'abonne aux changements d'état du jeu
        game.stateProperty().addListener(new ChangeListener<GameState>() {
            @Override
            public void changed(ObservableValue<? extends GameState> obs, GameState ancien, GameState nouveau){
                onEtatChange(nouveau);
            }
        });
    }

    private void onEtatChange(GameState nouvelEtat) {
        // on sauvegarde a la fin de chaque tour et quand la partie est terminée
        if (nouvelEtat != GameState.TURN_END && nouvelEtat != GameState.GAME_OVER) {
            return;
        }
        if (services.getPersistenceService() == null){
            return;
        }

        try {
            GameDTO dto = new GameDTO(game) ;
            services.getPersistenceService().saveGame(dto);
        } catch (Exception e) {
            System.out.println("SaveService : erreur sauvegarde - " + e.getMessage());
        }
    }
}
