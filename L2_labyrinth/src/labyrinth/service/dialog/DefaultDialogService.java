package labyrinth.service.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class DefaultDialogService implements DialogService{

    // les listes d'ecouteurs
    private final List<Consumer<String>> messageListeners = new ArrayList<>();
    private final List<Consumer<FileRequest>> openFileListeners = new ArrayList<>();
    private final List<Consumer<FileRequest>> saveFileListeners = new ArrayList<>();
    private final List<Consumer<ProfilesManagerRequest>> profilesManagerListeners = new ArrayList<>();

    @Override
    public void showMessage(String message) {
        // on notifie tous les ecouteurs
        for (Consumer<String> ecouteur : messageListeners) {
            ecouteur.accept(message);
        }
    }

    @Override
    public CompletionStage<File> requestOpenFile(String titre) {
        // on crée la requête  le future est pas encore complété
        FileRequest requete = new FileRequest(titre);

        // on notifie les ecouteurs, la vue va afficher le FileChooser
        for (Consumer<FileRequest> ecouteur : openFileListeners) {
            ecouteur.accept(requete);
        }

        return requete.getFuture();
    }

    @Override
    public CompletionStage<File> requestSaveFile(String titre) {
        FileRequest requete = new FileRequest(titre);
        for (Consumer<FileRequest> ecouteur : saveFileListeners) {
            ecouteur.accept(requete);
        }
        return requete.getFuture();
    }

    @Override
    public void ajouterMessageListener(Consumer<String> listener) {
        messageListeners.add(listener);
    }

    @Override
    public void ajouterOpenFileListener(Consumer<FileRequest> listener) {
        openFileListeners.add(listener);
    }

    @Override
    public void ajouterSaveFileListener(Consumer<FileRequest> listener) {
        saveFileListeners.add(listener);
    }

    @Override
    public void ajouterProfilesManagerListener(Consumer<ProfilesManagerRequest> listener) {
        profilesManagerListeners.add(listener);
    }

    @Override
    public void requestShowProfilesManager() {
        ProfilesManagerRequest requete = new ProfilesManagerRequest();
        for (Consumer<ProfilesManagerRequest> ecouteur : profilesManagerListeners) {
            ecouteur.accept(requete);
        }
    }
}