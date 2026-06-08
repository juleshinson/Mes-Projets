package Tests;

import Etat.Action;
import Etat.EtatJeu;
import Etat.Joueur;

public class Successeur {
    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        EtatJeu e2 = e.successeur(new Action(1, 0, 0), Joueur.joueurBlanc);
        e2.getPlateau().afficher();
    }
}
