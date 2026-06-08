import javafx.scene.Scene;
import javafx.stage.Stage;
import labyrinth.model.*;
import labyrinth.service.Services;
import labyrinth.service.dialog.DefaultDialogService;
import labyrinth.service.dialog.DialogService;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.persistence.UserProfile;
import labyrinth.service.persistence.SqlPersistenceService;
import labyrinth.service.persistence.UserProfile;
import labyrinth.service.serialization.GameDTO;
import labyrinth.service.serialization.JsonSerializationService;
import labyrinth.service.serialization.SerializationService;
import labyrinth.util.MazeBuilder;
import labyrinth.util.MazeFormatter;
import labyrinth.util.collection.ArrayGrid;
import labyrinth.util.collection.Grid;
import labyrinth.view.GameView;

import java.util.List;

import java.util.Set;
import java.util.UUID;


public class Test {
    public static void main(String[] args) {

  /*
        Grid<Tile> grid = new ArrayGrid(3, 2);
        grid.set(0, 0, new Tile(TileKind.ELBOW, new StartingPosition(0), Direction.RIGHT));
        grid.set(1, 0, new Tile(TileKind.CORRIDOR, new None(), Direction.LEFT));
        grid.set(2, 0, new Tile(TileKind.ELBOW, new StartingPosition(1), Direction.DOWN));
        grid.set(0, 1, new Tile(TileKind.T_CROSS, new Treasure(0)));
        grid.set(1, 1, new Tile(TileKind.CORRIDOR, new Treasure(23)));
        grid.set(2, 1, new Tile(TileKind.T_CROSS, new None(), Direction.LEFT));
        Tile tile = new Tile(TileKind.T_CROSS, new None());
        Maze maze = new Maze(grid, tile, Set.of(), Set.of());
        MazeFormatter formatter = new MazeFormatter();
        System.out.println(formatter.format(maze));*/

        Game game = new Game();
        game.reset();
        game.init();
        /* //tester l'affichage des joueurs
        Maze maze = MazeBuilder.buildStandardMaze();
        Player p0 = new Player(0);
        Player p1 = new Player(1);
        Player p2 = new Player(2);
        Player p3 = new Player(3);

        //ajout des players
        maze.getTiles().get(0,0).addPlayer(p0);
        maze.getTiles().get(1,0).addPlayer(p1);
        maze.getTiles().get(0,1).addPlayer(p2);
        maze.getTiles().get(1,1).addPlayer(p3);
        */
        /*MazeFormatter formatter = new MazeFormatter();
        System.out.println(formatter.format(game.getMaze()));*/

        // faut ouvrir les slots avant d'appeler init sinon ya pas de joueurs
        // en fait init() le fait lui meme via open() donc c'est bon
        /*
        try {
            game.init();
            System.out.println("init ok - état : " + game.getState());
        } catch (Exception e) {
            System.out.println("ERREUR init : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // on sérialise
        JsonSerializationService service = new JsonSerializationService();
        GameDTO dto = null;
        String json = null;

        try {
            dto = new GameDTO(game);
            json = service.serialize(dto);
            System.out.println("serialisation ok");
            // on affiche juste le debut pour voir si c'est du vrai json
            System.out.println("json (100 premiers chars) : " + json.substring(0, Math.min(100, json.length())));
        } catch (Exception e) {
            System.out.println("ERREUR serialisation : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // on désérialise
        try {
            GameDTO dto2 = service.deserialize(json);
            System.out.println("deserialisation ok");
            System.out.println("état récupéré : " + dto2.getState());
            System.out.println("index joueur courant : " + dto2.getCurrentPlayerIndex());
            System.out.println("nb joueurs : " + dto2.getPlayers().size());
        } catch (Exception e) {
            System.out.println("ERREUR deserialisation : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // on teste updateFrom
        try {
            Game game2 = new Game();
            GameDTO dto3 = service.deserialize(json);
            game2.updateFrom(dto3);
            System.out.println("updateFrom ok");
            System.out.println("état game2 : " + game2.getState());
            System.out.println("joueur courant game2 : " + game2.getCurrentPlayer().getIndex());
        } catch (Exception e) {
            System.out.println("ERREUR updateFrom : " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }*/
        /*
        try {
            game.init();
            System.out.println("init ok - état : " + game.getState());
        } catch (Exception e) {
            System.out.println("ERREUR init : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        SerializationService serializationService = new JsonSerializationService();
        String json = null;
        try {
            labyrinth.service.serialization.GameDTO dto = new labyrinth.service.serialization.GameDTO(game);
            json = serializationService.serialize(dto);
            System.out.println("serialisation ok");
            System.out.println("debut json : " + json.substring(0, Math.min(80, json.length())));
        } catch (Exception e) {
            System.out.println("ERREUR serialisation : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // test TP9 - on vérifie que le dialogService notifie bien
        System.out.println("");

        DialogService dialogService = new DefaultDialogService();

        // on ajoute un ecouteur de messages
        dialogService.ajouterMessageListener(msg -> {
            System.out.println("message recu : " + msg);
        });

        // on ajoute un ecouteur de fichier open
        dialogService.ajouterOpenFileListener(requete -> {
            System.out.println("requete open reçue : " + requete.getTitre());
            // on simule l'utilisateur qui annule
            requete.setFile(null);
        });

        // on ajoute un ecouteur de fichier save
        dialogService.ajouterSaveFileListener(requete -> {
            System.out.println("requete save reçue : " + requete.getTitre());
            requete.setFile(null);
        });

        // test showMessage
        dialogService.showMessage("cest un test");

        // test requestOpenFile
        dialogService.requestOpenFile("Ouvrir un fichier test").thenAccept(fichier -> {
            // fichier est null car on a simulé une annulation
            System.out.println("callback open; fichier : " + fichier);
        });

        // test requestSaveFile
        dialogService.requestSaveFile("Sauvegarder test").thenAccept(fichier -> {
            System.out.println("callback save; fichier : " + fichier);
        });

        // test Services
        Services services = new Services(serializationService, dialogService);
        System.out.println("services ok - serializationService : " + services.getSerializationService());
        System.out.println("services ok - dialogService : " + services.getDialogService());
    }*/

        MazeFormatter formatter = new MazeFormatter();
        System.out.println(formatter.format(game.getMaze()));

        SerializationService serializationService = new JsonSerializationService();
        SqlPersistenceService bdd = null;

        try {
            bdd = new SqlPersistenceService(serializationService);
            System.out.println("connexion BDD ok");
        } catch (Exception e) {
            System.out.println("ERREUR ouverture BDD : " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // test saveGame
        try {
            GameDTO dto = new GameDTO(game);
            bdd.saveGame(dto);
            System.out.println("saveGame ok - uuid : " + dto.getUuid());
        } catch (Exception e) {
            System.out.println("ERREUR saveGame : " + e.getMessage());
            e.printStackTrace();
        }

        // test loadLatestGame
        try {
            GameDTO dtoCharge = bdd.loadLatestGame();
            if (dtoCharge != null) {
                System.out.println("loadLatestGame ok - état : " + dtoCharge.getState());
                System.out.println("uuid récupéré : " + dtoCharge.getUuid());
            } else {
                // ca devrait pas arriver puisqu'on vient de sauvegarder
                System.out.println("loadLatestGame : null - bizarre");
            }
        } catch (Exception e) {
            System.out.println("ERREUR loadLatestGame : " + e.getMessage());
            e.printStackTrace();
        }

        // test CRUD profils
        System.out.println("");
        System.out.println("TEST PROFILS");

        UUID uuidProfil = null;
        try {
            uuidProfil = bdd.createUserProfile("TestJoueur");
            System.out.println("createUserProfile ok - uuid : " + uuidProfil);
        } catch (Exception e) {
            System.out.println("ERREUR createUserProfile : " + e.getMessage());
            e.printStackTrace();
        }

        // test getUserProfiles
        try {
            List<UserProfile> profils = bdd.getUserProfiles();
            System.out.println("getUserProfiles ok - nb profils : " + profils.size());
            for (UserProfile p : profils) {
                System.out.println("  profil : " + p.name() + " / " + p.uuid());
            }
        } catch (Exception e) {
            System.out.println("ERREUR getUserProfiles : " + e.getMessage());
            e.printStackTrace();
        }

        // test getUserProfile
        if (uuidProfil != null) {
            try {
                UserProfile p = bdd.getUserProfile(uuidProfil);
                if (p != null) {
                    System.out.println("getUserProfile ok : " + p.name());
                } else {
                    System.out.println("getUserProfile : null - bizarre");
                }
            } catch (Exception e) {
                System.out.println("ERREUR getUserProfile : " + e.getMessage());
                e.printStackTrace();
            }

            // test setUserName
            try {
                bdd.setUserName(uuidProfil, "NouveauNom");
                UserProfile pModifie = bdd.getUserProfile(uuidProfil);
                System.out.println("setUserName ok - nouveau nom : " + pModifie.name());
            } catch (Exception e) {
                System.out.println("ERREUR setUserName : " + e.getMessage());
                e.printStackTrace();
            }

            // test deleteUserProfile
            try {
                bdd.deleteUserProfile(uuidProfil);
                UserProfile pSupprime = bdd.getUserProfile(uuidProfil);
                // devrait etre null maintenant
                System.out.println("deleteUserProfile ok - profil apres suppression : " + pSupprime);
            } catch (Exception e) {
                System.out.println("ERREUR deleteUserProfile : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
