package Tests;

import Etat.EtatJeu;
import Etat.IA;
import Etat.Joueur;

public class TestMiniMax {
    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        System.out.println("État initial: ");
        e.getPlateau().afficher();
        EtatJeu e2 = IA.meilleurCoupMinimaxAvecEval0(e, Joueur.joueurBlanc, 1);
        System.out.println("État choisi: ");
        e2.getPlateau().afficher();
    }
}
