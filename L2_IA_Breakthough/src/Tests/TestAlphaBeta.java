package Tests;

import Etat.EtatJeu;
import Etat.IA;
import Etat.Joueur;
import Evaluations.EvaluationBooster;

public class TestAlphaBeta {
    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        System.out.println("État initial :");
        e.getPlateau().afficher();
        EtatJeu e2 = IA.meilleurCoupAlphaBetaAvecEval(e,  Joueur.joueurBlanc, 1, new EvaluationBooster());
        System.out.println("État choisi avec alpha-beta :");
        e2.getPlateau().afficher();
    }
}