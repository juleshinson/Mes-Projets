package labyrinth.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import labyrinth.service.dialog.DialogService;
import labyrinth.service.dialog.FileRequest;
import labyrinth.viewmodel.UserProfilesViewModel;

import java.io.File;


public class DialogHandler {

    private final Stage stage;

    private ProfilesManagerWindow profilesManagerWindow;

    public DialogHandler(DialogService dialogService, Stage stage) {
        this.stage = stage;

        //on s'abonne aux messages
        dialogService.ajouterMessageListener(this::handleMessage);

        // on s'abonne aux demandes d'ouverture de fichier
        dialogService.ajouterOpenFileListener(this::handleOpenFileRequest);
        //on s'abonne aux demandes de sauvegarde
        dialogService.ajouterSaveFileListener(this::handleSaveFileRequest);

        dialogService.ajouterProfilesManagerListener(requete -> Platform.runLater(() ->{
                if (profilesManagerWindow != null) {
                    profilesManagerWindow.show();
                }
            }));
    }

    // appelé par BaseGameViewModel pour passer le viewmodel des profils
    public void setUserProfilesViewModel(UserProfilesViewModel vm) {
        profilesManagerWindow = new ProfilesManagerWindow(vm);
    }

    // affiche une Alert avec le message
    private void handleMessage(String message) {
        Platform.runLater(() -> {Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // affiche un FileChooser pour ouvrir un fichier
    private void handleOpenFileRequest(FileRequest requete) {
        Platform.runLater(() -> {FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(requete.getTitre());
            // on filtre sur les fichiers json
            FileChooser.ExtensionFilter filtre = new FileChooser.ExtensionFilter("Fichiers JSON", "*.json");
            fileChooser.getExtensionFilters().add(filtre);

            File fichier = fileChooser.showOpenDialog(stage);
            // si l'utilisateur annule fichier vaut null
            // on complete avec null pour debloquer les callbacks
            requete.setFile(fichier);
        });
    }

    // affiche un FileChooser pour sauvegarder
    private void handleSaveFileRequest(FileRequest requete) {
        Platform.runLater(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(requete.getTitre());
            FileChooser.ExtensionFilter filtre = new FileChooser.ExtensionFilter("Fichiers JSON", "*.json");
            fileChooser.getExtensionFilters().add(filtre);

            File fichier = fileChooser.showSaveDialog(stage);
            requete.setFile(fichier);
        });
    }
}