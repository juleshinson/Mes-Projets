package viewmodel;

import model.Book;
import javafx.beans.property.*;

public class BookViewModel {

    private final Book book;

    // proprietes JavaFX observables
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty genre;
    private final IntegerProperty year;
    private final IntegerProperty rating;
    private final BooleanProperty vu;
    private final StringProperty synopsis;
    private String imagePath;

    public BookViewModel(Book book, String imagePath) {
        this.book = book;
        this.imagePath = imagePath;

        // on initialise chaque propriete depuis le modele
        this.title = new SimpleStringProperty(book.getTitle());
        this.author = new SimpleStringProperty(book.getAuthor());
        this.genre  = new SimpleStringProperty(book.getGenre());
        this.year = new SimpleIntegerProperty(book.getYear());
        this.rating  = new SimpleIntegerProperty(book.getRating());
        this.vu  = new SimpleBooleanProperty(book.isVu());
        this.synopsis = new SimpleStringProperty(book.getSynopsis());

        // listeners pour synchroniser le Book quand une propriete change
        this.title.addListener((obs, o, n) -> {book.setTitle(n);});
        this.author.addListener((obs, o, n) -> book.setAuthor(n));
        this.genre.addListener((obs, o, n) -> book.setGenre(n));
        this.year.addListener((obs, o, n) -> book.setYear(n.intValue()));
        this.rating.addListener((obs, o, n) -> {book.setRating(n.intValue());});
        this.vu.addListener((obs, o, n) -> book.setVu(n));
        this.synopsis.addListener((obs, o, n) -> book.setSynopsis(n));
    }

    // proprietes observables (pour les bindings dans la vue)
    public StringProperty  titleProperty(){ return title; }
    public StringProperty  authorProperty() { return author; }
    public StringProperty  genreProperty()  { return genre; }
    public IntegerProperty yearProperty()  { return year; }
    public IntegerProperty ratingProperty()  { return rating; }
    public BooleanProperty vuProperty() { return vu; }
    public StringProperty  synopsisProperty(){ return synopsis; }

    // getters classiques
    public String  getTitle()  { return title.get(); }
    public String  getAuthor()  { return author.get(); }
    public String  getGenre() { return genre.get(); }
    public int     getYear() { return year.get(); }
    public int     getRating()  { return rating.get(); }
    public boolean isVu() { return vu.get(); }
    public String  getSynopsis() { return synopsis.get(); }
    public String  getImagePath(){ return imagePath; }
    public Book    getBook() { return book; }

    // setters
    public void setTitle(String v) { title.set(v); }
    public void setAuthor(String v){ author.set(v); }
    public void setGenre(String v) { genre.set(v); }
    public void setYear(int v) { year.set(v); }
    public void setRating(int v)  { rating.set(v); }
    public void setVu(boolean v)  { vu.set(v); }
    public void setSynopsis(String v) { synopsis.set(v); }
    public void setImagePath(String v){ this.imagePath = v; }

    // affiche le titre dans la listview
    @Override
    public String toString() {
        return title.get();
    }
}