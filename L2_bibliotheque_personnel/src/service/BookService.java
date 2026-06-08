package service;

import model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

// service qui gere tout ce qui touche au JSON
public class BookService {

    public List<Book> chargerLivres(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                sb.append(ligne);
            }
            //System.out.println("JSON charge : " + sb.length() + " caracteres");

            JSONArray tableau = new JSONArray(sb.toString());
            List<Book> liste = new ArrayList<>();
            for (int i = 0; i < tableau.length(); i++) {
                liste.add(jsonVersBook(tableau.getJSONObject(i)));
            }
            //System.out.println("Nombre de livres charges : " + liste.size());
            return liste;
        } catch (Exception e) {
            throw new RuntimeException("Erreur chargement : " + e.getMessage());
        }
    }

    public List<Book> importerFichier(File fichier) {
        try {
            // deserialisation du fichier json
            String contenu = Files.readString(fichier.toPath(), StandardCharsets.UTF_8);
            JSONArray tab = new JSONArray(contenu);
            List<Book> livres = new ArrayList<>();
            for (int i = 0; i < tab.length(); i++) {
                livres.add(jsonVersBook(tab.getJSONObject(i)));
            }
            return livres;
        } catch (Exception e) {
            throw new RuntimeException("Erreur import : " + e.getMessage());
        }
    }

    // serialisation : on transforme chaque livre en objet JSON
    public void exporterFichier(File fichier, Iterable<Book> livres) {
        try {
            JSONArray tableau = new JSONArray();
            for (Book b : livres) {
                JSONObject obj = new JSONObject();
                obj.put("title", b.getTitle());
                obj.put("author",b.getAuthor());
                obj.put("genre",b.getGenre());
                obj.put("year",b.getYear());
                obj.put("rating",b.getRating());
                obj.put("vu", b.isVu());
                obj.put("synopsis",b.getSynopsis());
                tableau.put(obj);
            }
            // on ecrit le json dans le fichier avec une indentation de 2
            Files.writeString(fichier.toPath(), tableau.toString(2), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erreur export : " + e.getMessage());
        }
    }

    public String getImageDuGenre(String genre) {
        // on remplace les caracteres speciaux pour avoir un nom de fichier valide
        String nom = genre.toLowerCase()
                .replace("é","e")
                .replace("è","e")
                .replace("ê","e")
                .replace("-", "")
                .replace("â","a");
        return "/images/" + nom + ".png";
    }

    // convertit un JSONObject en Book
    private Book jsonVersBook(JSONObject obj) {
        String titre= obj.optString("title", "");
        String auteur = obj.optString("author","");
        String genre  = obj.optString("genre","");
        int annee= obj.optInt("year",0);
        String synopsis = obj.optString("synopsis","");
        int note= obj.optInt("rating",0);
        boolean lu = obj.optBoolean("vu",false);
        return new Book(titre, auteur, genre, annee, synopsis, note, lu);
    }
}