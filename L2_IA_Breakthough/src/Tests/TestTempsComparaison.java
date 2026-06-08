package Tests;

import Etat.EtatJeu;
import Etat.IA;
import Etat.Joueur;
import Evaluations.Evaluation;
import Evaluations.EvaluationBooster;

public class TestTempsComparaison {

    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        Joueur machine = Joueur.joueurBlanc;
        Evaluation booster = new EvaluationBooster();
        for (int profondeur = 1; profondeur <= 3; profondeur++) {

            System.out.println("-----------------------------------");
            System.out.println("Profondeur : " + profondeur);

            //Temps pour minimax simple.
            long debutMinimax = System.nanoTime();
            EtatJeu m1 = IA.meilleurCoupMinimaxAvecEval0(e, machine, profondeur);
            long finMinimax = System.nanoTime();
            long dureeMinimax = finMinimax - debutMinimax;

            //Temps pour alpha-beta avec Evaluation booster

            long debutAlphaBeta = System.nanoTime();
            EtatJeu m2 = IA.meilleurCoupAlphaBetaAvecEval(e, machine, profondeur, booster);
            long finAlphaBeta = System.nanoTime();
            long dureeAlphaBeta = finAlphaBeta - debutAlphaBeta;

            //Affichage et conversion des temps
            System.out.println("Temps Minimax simple (ns) : " + dureeMinimax);
            System.out.println("Temps alpha-beta (ns) : " + dureeAlphaBeta);
            System.out.println("Temps Minimax simple (ms) : " + (dureeMinimax / 1000000.0));
            System.out.println("Temps alpha-beta (ms) : " + (dureeAlphaBeta / 1000000.0));
        }
    }
}