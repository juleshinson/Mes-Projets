package Tests;

import Etat.Action;
import Etat.EtatJeu;
import Etat.Joueur;

public class CoupValide {
    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        Action a = new Action(1, 0, 0);
        System.out.println(e.actionValide(a, Joueur.joueurBlanc));
    }
}
