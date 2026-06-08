package labyrinth;

import labyrinth.model.Game;
import labyrinth.service.Services;
import labyrinth.service.serialization.GameDTO;

// s'occupe de charger la derniere partie sauvegardée en BDD au démarrage
// si ya rien en BDD le jeu reste en état SETUP
public class GameInitializer {

    private final Game game;
    private final Services services;

    public GameInitializer(Game game, Services services) {
        this.game = game;
        this.services = services;
    }

    public void initGame() {
        // si pas de service de persistance on fait rien
        if (services.getPersistenceService() == null){
            //System.out.println("GameInitializer :pas de BDD disponible, partie vierge");
            return;
        }
        try {
            GameDTO derniereSauvegarde = services.getPersistenceService().loadLatestGame();
            if (derniereSauvegarde != null){
                // on recharge la derniere partie
                game.updateFrom(derniereSauvegarde) ;
            }else {
                System.out.println("GameInitializer : aucune sauvegarde trouvée");
            }

        } catch (Exception e) {

            game.hardReset();
        }
    }
}
