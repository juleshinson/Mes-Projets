package labyrinth.viewmodel;

import javafx.application.Platform;
import labyrinth.model.Game;
import labyrinth.service.Services;
import labyrinth.service.serialization.GameDTO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class BaseMenuViewModel implements MenuViewModel {

    private final Game game;
    private final Services services;

    public BaseMenuViewModel() {
        this(null, null);
    }

    public BaseMenuViewModel(Game game, Services services) {
        this.game = game;
        this.services = services;
    }

    public void newGame() {
        System.out.println("Nouveau jeu");
    }


    @Override
    public void loadGame() {
        // si pas de services on ne peut rien faire
        if (services == null) {
            System.out.println("pas de services disponible");
            return;
        }

        // on demande a l'utilisateur de choisir le fichier a charger
        services.getDialogService().requestOpenFile("Charger une partie").thenAccept(fichier -> {
            // fichier peut etre null si l'utilisateur annule
            if (fichier == null) {
                System.out.println("chargement annulé");
                return;
            }
            chargerDepuisFichier(fichier);
        });
    }

    private void chargerDepuisFichier(File fichier) {
        try {
            // on lit tout le contenu du fichier
            FileReader reader = new FileReader(fichier);
            StringBuilder contenu = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                contenu.append((char) c);
            }
            reader.close();

            // on deserialise
            String json = contenu.toString();
            GameDTO dto = services.getSerializationService().deserialize(json);

            // on hydrate le jeu courant
            game.updateFrom(dto);

            System.out.println("chargement ok depuis " + fichier.getName());

        } catch (Exception e){
            System.out.println("erreur chargement : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void saveGame() {
        if (services == null) {
            System.out.println("pas de services disponible");
            return;
        }

        // on demande où sauvegarder
        services.getDialogService().requestSaveFile("Sauvegarder la partie").thenAccept(fichier -> {
            if (fichier == null) {
                System.out.println("sauvegarde annulée");
                return;
            }
            sauvegarderVersFichier(fichier);
        });
    }

    private void sauvegarderVersFichier(File fichier) {
        try {
            // on cree le DTO depuis le jeu courant
            GameDTO dto = new GameDTO(game);

            // on serialise
            String json = services.getSerializationService().serialize(dto);

            // on ecrit dans le fichier
            FileWriter writer = new FileWriter(fichier);
            writer.write(json);
            writer.close();
            System.out.println("sauvegarde ok dans " + fichier.getName());

        }catch (Exception e){
            System.out.println("erreur sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exit() {
        Platform.exit();
    }


    public void showAbout() {
        System.out.println("A propos de ce jeu qui m'a pris des heures");
    }

    @Override
    public void manageProfiles() {
        if (services == null) {
            return;
        }
        // on demande au dialogService d'afficher la fenêtre de gestion des profils
        services.getDialogService().requestShowProfilesManager();
    }
}
