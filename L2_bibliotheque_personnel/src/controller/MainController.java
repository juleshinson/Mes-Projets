package controller;

import javafx.stage.Stage;
import service.BookService;
import view.SuggestionDialog;
import viewmodel.BookViewModel;
import viewmodel.LibraryViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.InputStream;

// controleur FXML = la Vue dans l'archi MVVM
// s'occupe uniquement de l'affichage et de transmettre les actions au ViewModel
public class MainController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField yearField;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ComboBox<String> genreComboBox;
    @FXML private ListView<BookViewModel> booksListView;
    @FXML private ImageView bookImageView;
    @FXML private CheckBox vu;
    @FXML private Label detailsLabel;
    @FXML private Label synopsisLabel;
    @FXML private TextArea synopsisField;
    @FXML private Button etoile1;
    @FXML private Button etoile2;
    @FXML private Button etoile3;
    @FXML private Button etoile4;
    @FXML private Button etoile5;

    // note selectionnee avec les etoiles
    private int noteCourante = 0;

    private LibraryViewModel libraryViewModel;

    @FXML
    public void initialize() {
        BookService service = new BookService();
        InputStream is = getClass().getResourceAsStream("/books.json");

        // si le fichier est pas trouve ca va planter ici
        libraryViewModel = new LibraryViewModel(service, is);

        booksListView.setItems(libraryViewModel.getListeTriee());

        sortComboBox.getItems().addAll("Titre A-Z", "Titre Z-A", "Auteur A-Z", "Auteur Z-A",
                "Année croissante", "Année décroissante", "Note croissante", "Note décroissante");
        genreComboBox.getItems().addAll("Tous", "Roman", "Science-fiction", "Fantastique",
                "Policier", "Aventure", "Biographie", "Historique", "Poésie", "Théâtre",
                "Manga", "Thriller", "Essai");

        // selection d'un livre dans la liste -> on affiche ses details
        booksListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    if (selected != null) {
                        detailsLabel.textProperty().unbind();
                        synopsisLabel.textProperty().unbind();

                        // binding du label details sur les proprietes du livre selectionne
                        detailsLabel.textProperty().bind(
                                selected.titleProperty()
                                        .concat("\n\nAuteur : ").concat(selected.authorProperty())
                                        .concat("\n\nGenre : ").concat(selected.genreProperty())
                                        .concat("\n\nAnnée : ").concat(selected.yearProperty())
                                        .concat("\n\nNote : ").concat(selected.ratingProperty()).concat("/5")
                                        .concat("\n\nDéjà lu : ").concat(selected.vuProperty().map(v -> v ? "Oui" : "Non"))
                        );
                        synopsisLabel.textProperty().bind(selected.synopsisProperty());

                        InputStream img = getClass().getResourceAsStream(selected.getImagePath());
                        bookImageView.setImage(img != null ? new Image(img) : null);
                    }
                });

        // recherche textuelle
        searchField.textProperty().addListener((obs, old, texte) ->
                libraryViewModel.filtrerLivres(texte, genreComboBox.getValue()));

        // tri
        sortComboBox.valueProperty().addListener((obs, old, valeur) -> {
            if (valeur != null) {
                libraryViewModel.trierLivres(valeur);
            }
        });

        // filtre par genre
        genreComboBox.valueProperty().addListener((obs, old, valeur) ->
                libraryViewModel.filtrerLivres(searchField.getText(), valeur));
    }

    @FXML
    private void onAdd() {
        if (titleField.getText().isEmpty() || authorField.getText().isEmpty()
                || genreField.getText().isEmpty() || yearField.getText().isEmpty()) {
            showError("Veuillez remplir le titre, l'auteur, le genre et l'année.");
            return;
        }

        int annee;
        try {
            annee = Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            showError("L'année doit être un nombre.");
            return;
        }

        if (libraryViewModel.estEnEdition()) {
            // mode edition : on valide les modifications
            libraryViewModel.validerEdition(
                    titleField.getText(), authorField.getText(), genreField.getText(),
                    annee, synopsisField.getText(), noteCourante, vu.isSelected());
            viderChamps();
            showInfo("Livre modifié avec succès.");
            return;
        }

        // mode ajout
        BookViewModel nouveau = libraryViewModel.ajouterBook(
                titleField.getText(), authorField.getText(), genreField.getText(),
                annee, synopsisField.getText(), noteCourante, vu.isSelected());
        booksListView.getSelectionModel().select(nouveau);
        viderChamps();
        showInfo("Livre ajouté avec succès.");
    }

    @FXML
    private void onEdit() {
        BookViewModel selectionne = booksListView.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showError("Veuillez sélectionner un livre à modifier.");
            return;
        }

        libraryViewModel.commencerEdition(selectionne);

        // on prefill les champs avec les valeurs du livre
        titleField.setText(selectionne.getTitle());
        authorField.setText(selectionne.getAuthor());
        genreField.setText(selectionne.getGenre());
        yearField.setText(String.valueOf(selectionne.getYear()));
        synopsisField.setText(selectionne.getSynopsis());
        vu.setSelected(selectionne.isVu());
        noteCourante = selectionne.getRating();
        updateStars();

        showInfo("Mode édition activé. Modifiez les champs puis cliquez sur Ajouter pour valider.");
    }

    @FXML
    private void onDelete() {
        BookViewModel selectionne = booksListView.getSelectionModel().getSelectedItem();
        if (selectionne == null) {
            showError("Veuillez sélectionner un livre à supprimer.");
            return;
        }

        // TODO: ajouter une confirmation avant de supprimer ?
        libraryViewModel.supprimerBook(selectionne);
        viderChamps();
        detailsLabel.setText("Sélectionnez un livre pour voir les détails");
        synopsisLabel.setText("");
        bookImageView.setImage(null);
        showInfo("Livre supprimé avec succès.");
    }

    private void viderChamps() {
        detailsLabel.textProperty().unbind();
        synopsisLabel.textProperty().unbind();
        titleField.clear();
        authorField.clear();
        genreField.clear();
        yearField.clear();
        synopsisField.clear();
        vu.setSelected(false);
        noteCourante = 0;
        updateStars();
    }

    @FXML
    private void onExport() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Exporter la collection");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier JSON", "*.json"));
        File fichier = fc.showSaveDialog(booksListView.getScene().getWindow());
        if (fichier == null) {
            return;
        }
        try {
            libraryViewModel.exporterCollection(fichier);
            showInfo("Collection exportée avec succès.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onImport() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Importer une collection");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier JSON", "*.json"));
        File fichier = fc.showOpenDialog(booksListView.getScene().getWindow());
        if (fichier == null) {
            return;
        }
        try {
            libraryViewModel.importerCollection(fichier);
            viderChamps();
            showInfo("Collection importée avec succès.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // gestion des etoiles de notation
    @FXML private void rate1() { noteCourante = 1; updateStars(); }
    @FXML private void rate2() { noteCourante = 2; updateStars(); }
    @FXML private void rate3() { noteCourante = 3; updateStars(); }
    @FXML private void rate4() { noteCourante = 4; updateStars(); }
    @FXML private void rate5() { noteCourante = 5; updateStars(); }

    private void updateStars() {
        Button[] etoiles = { etoile1, etoile2, etoile3, etoile4, etoile5 };
        for (int i = 0; i < etoiles.length; i++) {
            etoiles[i].setStyle(i < noteCourante ? "-fx-text-fill: gold;" : "-fx-text-fill: gray;");
        }
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void onSuggestion() {
        SuggestionDialog dialog = new SuggestionDialog(libraryViewModel);
        dialog.afficher((Stage) booksListView.getScene().getWindow());
    }
}