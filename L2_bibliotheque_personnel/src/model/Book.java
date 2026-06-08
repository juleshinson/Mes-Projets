package model;

// classe qui represente un livre dans la collection
public class Book {

    private String title;
    private String author;
    private String genre;
    private int year;
    private String synopsis;
    private int rating; // note entre 1 et 5
    private boolean vu; // true si le livre a deja ete lu

    public Book(String title, String author, String genre, int year, String synopsis, int rating, boolean vu) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.synopsis = synopsis;
        this.rating = rating;
        this.vu = vu;
    }

    // getters et setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public boolean isVu() { return vu; }
    public void setVu(boolean vu) { this.vu = vu; }

    // toString pour debug
    @Override
    public String toString() {
        return title + " - " + author;

    }
}