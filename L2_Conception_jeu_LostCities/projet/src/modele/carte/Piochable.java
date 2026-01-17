package modele.carte;

// L'interface Piochable représente un paquet dans lequel on peut piocher une carte.
// Elle étend l'interface Paquet, donc elle hérite de toutes les méthodes liées à la gestion d'un ensemble de cartes.
public interface Piochable extends Paquet {

    // Méthode qui permet de prendre une carte depuis le paquet
    // La façon de prendre la carte dépendra de l'implémentation
    public abstract Carte prendreCarte();
}

