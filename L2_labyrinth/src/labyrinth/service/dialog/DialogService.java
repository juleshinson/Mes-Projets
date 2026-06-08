package labyrinth.service.dialog;

import java.io.File;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public interface DialogService {

    // affiche un message à l'utilisateur
    void showMessage(String message);

    // demande à l'utilisateur de choisir un fichier à ouvrir
    CompletionStage<File> requestOpenFile(String titre);

    // demande à l'utilisateur de choisir où sauvegarder
    CompletionStage<File> requestSaveFile(String titre);

    // pour s'abonner aux messages
    void ajouterMessageListener(Consumer<String> listener);

    // pour s'abonner aux demandes d'ouverture de fichier
    void ajouterOpenFileListener(Consumer<FileRequest> listener);

    // pour s'abonner aux demandes de sauvegarde
    void ajouterSaveFileListener(Consumer<FileRequest> listener);

    void requestShowProfilesManager();

    void ajouterProfilesManagerListener(Consumer<ProfilesManagerRequest> listener);

}