package labyrinth;

import labyrinth.model.Game;
import labyrinth.service.Services;

// crée le Game et le passe à GameInitializer et SaveService
public class GameBootstrapper {

    private final Game game ;

    public GameBootstrapper(Services services) {
        this.game = new Game() ;

        // on essaie de charger la derniere partie depuis la BDD
        GameInitializer initializer = new GameInitializer(game, services) ;
        initializer.initGame() ;

        // on attache le service de sauvegarde automatique
        new SaveService(game, services) ;
    }

    public Game getGame() {
        return game ;
    }
}
