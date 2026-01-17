package modele.strategie;

import modele.Coup;
import modele.Jeu;
import modele.carte.Couleur;

// L'interface Strategie définit le comportement d'un joueur.
// Elle permet de séparer la logique du jeu de la façon dont les décisions sont prises
// (humain, bot simple, bot intelligent, etc.).
public interface Strategie {

    // Méthode qui doit retourner le coup choisi par la stratégie
    // Le jeu est passé en paramètre pour permettre à la stratégie d'analyser l'état actuel de la partie
    public abstract Coup choisirCoup(Jeu jeu);

    // Méthode qui doit retourner le choix de pioche
    public abstract Couleur choisirPioche(Jeu jeu);
}

