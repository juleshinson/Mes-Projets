package modele.carte;

import java.util.Collection;

// La classe Pioche représente la pioche du jeu.
// C'est l'endroit où se trouvent toutes les cartes qui n'ont pas encore été distribuées ou piochées par les joueurs.

public class Pioche extends PaquetBase implements Piochable {

    // Constructeur de la pioche
    // Il appelle simplement le constructeur de la classe mère
    // pour initialiser une pioche vide
    public Pioche() {
        super();
    }

    // Méthode permettant de piocher une carte depuis la pioche
    // Elle est appelée quand un joueur décide de piocher dans la pioche principale
    @Override
    public Carte prendreCarte() {

        // Si la pioche est vide, il n'y a plus de carte à prendre
        // On retourne donc null
        if (estVide()) {
            return null;
        }

        // Sinon, on prend la première carte de la pioche
        return prendrePremiereCarte();
    }

    // Méthode d'initialisation de la pioche avec un nombre variable de cartes
    // Les cartes sont ajoutées à la pioche puis mélangées
    public void init(Carte... cartes) {
        ajouterCartes(cartes); // ajout des cartes à la pioche
        melanger();            // mélange pour rendre l'ordre aléatoire
    }

    // Méthode d'initialisation de la pioche avec une collection de cartes
    public void init(Collection<Carte> cartes) {
        ajouterCartes(cartes); // ajout de toutes les cartes de la collection
        melanger();            // mélange de la pioche
    }
}

