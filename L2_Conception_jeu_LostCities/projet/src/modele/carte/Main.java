package modele.carte;

import java.util.Collection;

// La classe Main représente la main d'un joueur.
// Elle contient les cartes que le joueur a en main à un instant donné.
// Elle hérite de PaquetBase, qui gère la liste des cartes et les opérations de base.
public class Main extends PaquetBase {

    // Constructeur de la main
    // Il appelle simplement le constructeur de la classe mère
    // Cela initialise une main vide
    public Main() {
        super();
    }

    // Méthode permettant d'ajouter une ou plusieurs cartes à la main
    public void ajouterCartes(Carte... cartes) {
        super.ajouterCartes(cartes);
    }

    // Méthode permettant d'ajouter une collection de cartes à la main
    public void ajouterCartes(Collection<Carte> cartes) {
        super.ajouterCartes(cartes);
    }

    // Méthode permettant de prendre une carte de la main à un indice donné
    // La carte est retirée de la main et retournée
    // L'indice correspond à la position de la carte dans la main
    public Carte prendreCarte(int indice) {
        return super.prendreCarte(indice);
    }
}

