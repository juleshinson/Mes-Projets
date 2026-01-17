package modele.carte;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// L'interface Paquet représente un ensemble de cartes.
// Elle définit les opérations de base que tout paquet de cartes doit proposer.
public interface Paquet extends Iterable<Carte> {

    // Méthode qui retourne le nombre de cartes présentes dans le paquet
    // Elle est abstraite car chaque implémentation décide comment stocker les cartes
    public abstract int getNbCartes();

    // Méthode qui permet de voir une carte à un indice précis
    // Elle ne retire pas la carte du paquet, elle permet seulement de la consulter
    public abstract Carte voirCarte(int indice);

    // Méthode par défaut qui indique si le paquet est vide
    // Un paquet est vide s'il contient zéro carte
    default boolean estVide() {
        return getNbCartes() == 0;
    }

    // Méthode par défaut qui retourne la première carte du paquet
    // Si le paquet est vide, on retourne null pour éviter une erreur
    default Carte voirPremiereCarte() {
        if (estVide()) {
            return null;
        }
        return voirCarte(0);
    }

    // Méthode par défaut qui retourne la dernière carte du paquet
    // Si le paquet est vide, on retourne null
    default Carte voirDernierCarte() {
        if (estVide()) {
            return null;
        }
        return voirCarte(getNbCartes() - 1);
    }

    // Méthode par défaut qui permet de récupérer un Stream de cartes
    // Cela permet d'utiliser facilement les streams Java
    // Le Stream est créé à partir de l'itérateur fourni par Iterable
    default Stream<Carte> cartes() {
        return StreamSupport.stream(this.spliterator(), false);
    }
}

