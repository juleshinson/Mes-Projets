package labyrinth.service.dialog;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

// une requête de fichier  contient le CompletableFuture qui sera complété
// quand l'utilisateur choisit un fichier
public class FileRequest {

    private final CompletableFuture<File> fileFuture;
    private final String titre;

    public FileRequest(String titre) {
        this.titre = titre;
        // le future est pas encore complété à la création
        this.fileFuture = new CompletableFuture<>();
    }

    public String getTitre() {
        return titre;
    }

    // appelé par la vue quand l'utilisateur a choisi son fichier
    // ca complete le future et déclenche les callbacks
    public void setFile(File file) {
        fileFuture.complete(file);
    }

    public CompletionStage<File> getFuture() {
        return fileFuture;
    }
}