package viewmodel;

import model.Book;
import service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.io.File;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

// ViewModel principal - gere la liste, le filtre, le tri et le CRUD
public class LibraryViewModel {

    private final BookService bookService;

    // liste maitre de tous les livres
    private final ObservableList<BookViewModel> bookList = FXCollections.observableArrayList();

    private final FilteredList<BookViewModel> listeFiltree;
    private final SortedList<BookViewModel> listeTriee;

    // livre en cours d'edition (null = mode ajout)
    private BookViewModel enCoursEdition = null;

    public LibraryViewModel(BookService bookService, InputStream is) {
        this.bookService = bookService;
        this.listeFiltree = new FilteredList<>(bookList, b -> true);
        this.listeTriee = new SortedList<>(listeFiltree);

        // chargement initial des livres
        List<Book> livresCharges = bookService.chargerLivres(is);
        for (Book b : livresCharges) {
            bookList.add(versViewModel(b));
        }
        //System.out.println("LibraryViewModel init : " + bookList.size() + " livres charges");
    }

    public SortedList<BookViewModel> getListeTriee() {
        return listeTriee;
    }

    // --- Filtre et tri ---

    public void filtrerLivres(String recherche, String genre) {
        String texte = recherche == null ? "" : recherche.toLowerCase();
        listeFiltree.setPredicate(book -> {
            boolean matchTexte = texte.isEmpty() || book.getTitle().toLowerCase().contains(texte) || book.getAuthor().toLowerCase().contains(texte);
            boolean matchGenre = genre == null || genre.equals("Tous") || book.getGenre().equalsIgnoreCase(genre);
            return matchTexte && matchGenre;
        });
    }

    public void trierLivres(String choixTri) {
        Comparator<BookViewModel> comp = switch (choixTri) {
            case "Titre A-Z" -> Comparator.comparing(BookViewModel::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "Titre Z-A" -> Comparator.comparing(BookViewModel::getTitle, String.CASE_INSENSITIVE_ORDER).reversed();
            case "Auteur A-Z"  -> Comparator.comparing(BookViewModel::getAuthor, String.CASE_INSENSITIVE_ORDER);
            case "Auteur Z-A" -> Comparator.comparing(BookViewModel::getAuthor, String.CASE_INSENSITIVE_ORDER).reversed();
            case "Année croissante" -> Comparator.comparingInt(BookViewModel::getYear);
            case "Année décroissante"-> Comparator.comparingInt(BookViewModel::getYear).reversed();
            case "Note croissante" -> Comparator.comparingInt(BookViewModel::getRating);
            case "Note décroissante"-> Comparator.comparingInt(BookViewModel::getRating).reversed();
            default -> null;
        };
        listeTriee.setComparator(comp);
    }

    // --- CRUD ---

    public BookViewModel ajouterBook(String titre, String auteur, String genre, int annee, String synopsis, int note, boolean vu) {
        Book nouveauLivre = new Book(titre, auteur, genre, annee, synopsis, note, vu);
        BookViewModel vm = versViewModel(nouveauLivre);
        bookList.add(vm);
        return vm;
    }

    public void commencerEdition(BookViewModel book) {
        this.enCoursEdition = book;
    }

    public BookViewModel validerEdition(String titre, String auteur, String genre, int annee, String synopsis, int note, boolean vu) {
        enCoursEdition.setTitle(titre);
        enCoursEdition.setAuthor(auteur);
        enCoursEdition.setGenre(genre);
        enCoursEdition.setYear(annee);
        enCoursEdition.setRating(note);
        enCoursEdition.setVu(vu);
        enCoursEdition.setSynopsis(synopsis);
        enCoursEdition.setImagePath(bookService.getImageDuGenre(genre));
        BookViewModel modifie = enCoursEdition;
        enCoursEdition = null;
        return modifie;
    }

    public void supprimerBook(BookViewModel book) {
        bookList.remove(book);
    }

    public boolean estEnEdition() {
        return enCoursEdition != null;
    }

    // --- Import / Export ---

    public void importerCollection(File fichier) {
        bookList.clear();
        List<Book> importes = bookService.importerFichier(fichier);
        for (Book b : importes) {
            bookList.add(versViewModel(b));
        }
    }

    public void exporterCollection(File fichier) {
        // on recupere les Book sous-jacents pour les serialiser
        List<Book> livres = bookList.stream().map(BookViewModel::getBook).toList();
        bookService.exporterFichier(fichier, livres);
    }

    // --- Suggestion ---

    public BookViewModel suggererBook(boolean nonLuSeulement, int noteMinimum, String genre) {
        List<BookViewModel> candidats = bookList.stream()
                .filter(b -> !nonLuSeulement || !b.isVu())
                .filter(b -> b.getRating() >= noteMinimum)
                .filter(b -> genre == null || b.getGenre().equalsIgnoreCase(genre))
                .toList();

        if (candidats.isEmpty()) {
            return null;
        }

        int index = new Random().nextInt(candidats.size());
        return candidats.get(index);
    }

    // convertit un Book en BookViewModel avec l'image correspondante
    private BookViewModel versViewModel(Book book) {
        String img = bookService.getImageDuGenre(book.getGenre());
        return new BookViewModel(book, img);
    }
}