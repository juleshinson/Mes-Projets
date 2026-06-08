package view;

import viewmodel.BookViewModel;
import viewmodel.LibraryViewModel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

// fenetre de suggestion : pose des questions et propose un livre
public class SuggestionDialog {

    private final LibraryViewModel libraryViewModel;

    public SuggestionDialog(LibraryViewModel libraryViewModel) {
        this.libraryViewModel = libraryViewModel;
    }

    public void afficher(Stage parentStage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle("Je ne sais pas quoi lire...");
        dialog.setResizable(false);

        VBox root = new VBox(12);
        root.setPadding(new Insets(25));

        Label titre = new Label("  Aide-moi à choisir !");
        titre.getStyleClass().add("titre-suggestion");

        // question 1
        Label q1 = new Label("Tu veux un livre que tu n'as pas encore lu ?");
        ComboBox<String> rep1 = new ComboBox<>();
        rep1.getItems().addAll("Oui, pas encore lu", "Peu importe");
        rep1.setValue("Oui, pas encore lu");
        rep1.setMaxWidth(Double.MAX_VALUE);

        // question 2 : note min
        Label q2 = new Label("Note minimale ?");
        ComboBox<String> rep2 = new ComboBox<>();
        rep2.getItems().addAll("Peu importe", "★★★ minimum", "★★★★ minimum", "★★★★★ seulement");
        rep2.setValue("Peu importe");
        rep2.setMaxWidth(Double.MAX_VALUE);

        // question 3
        Label q3 = new Label("Un genre en particulier ?");
        ComboBox<String> rep3 = new ComboBox<>();
        rep3.getItems().addAll("Peu importe", "Roman", "Science-fiction", "Fantastique",
                "Policier", "Aventure", "Biographie", "Historique",
                "Poésie", "Théâtre", "Manga", "Thriller", "Essai");
        rep3.setValue("Peu importe");
        rep3.setMaxWidth(Double.MAX_VALUE);

        Label resultat = new Label("");
        resultat.getStyleClass().add("label-suggestion");
        resultat.setWrapText(true);

        Button btnTrouver = new Button(" Trouver un livre !");
        btnTrouver.setMaxWidth(Double.MAX_VALUE);
        btnTrouver.getStyleClass().add("btn-ajouter");

        btnTrouver.setOnAction(e -> {boolean nonLuSeulement = rep1.getValue().equals("Oui, pas encore lu");

            int noteMin = switch (rep2.getValue()) {
                case "★★★ minimum"     -> 3;
                case "★★★★ minimum"    -> 4;
                case "★★★★★ seulement" -> 5;
                default -> 0;
            };

            // null si "Peu importe" sinon on prend le genre choisi
            String genre = rep3.getValue().equals("Peu importe") ? null : rep3.getValue();

            BookViewModel suggestion = libraryViewModel.suggererBook(nonLuSeulement, noteMin, genre);

            if (suggestion == null) {
                resultat.setText(" Aucun livre ne correspond...\nEssaie d'élargir tes critères !");
            } else {
                resultat.setText(
                        "  Je te conseille :\n\n" +
                                "\"" + suggestion.getTitle() + "\"\n" +
                                "de " + suggestion.getAuthor() + "\n" +
                                "⭐ " + suggestion.getRating() + "/5  •  " + suggestion.getGenre()
                );
            }
        });

        root.getChildren().addAll(titre, q1, rep1, q2, rep2, q3, rep3, btnTrouver, resultat);

        Scene scene = new Scene(root, 420, 480);
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
        dialog.setScene(scene);
        dialog.show();
    }
}