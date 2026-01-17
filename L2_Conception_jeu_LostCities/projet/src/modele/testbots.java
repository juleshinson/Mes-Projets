package modele;

import modele.carte.Couleur;
import modele.strategie.BotDefausse;
import modele.strategie.BotMin;

public class testbots {
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
// Configurer les strategies des joueurs
        jeu.getJoueur(0).setStrategie(new BotDefausse());
        jeu.getJoueur(1).setStrategie(new BotMin());
        while (!jeu.estFini()) {
            Joueur joueur = jeu.getJoueurCourant();
            Coup coup = joueur.choisirCoup(jeu);
            jeu.jouer(coup);
            Couleur pioche = joueur.choisirPioche(jeu);
            jeu.piocher(pioche);
            jeu.passerJoueurSuivant();
        }
// Afficher les scores finaux
        System.out.println("Score joueur 0 : " +
                jeu.getJoueur(0).getScore());
        System.out.println("Score joueur 1 : " +
                jeu.getJoueur(1).getScore());
    }
}
