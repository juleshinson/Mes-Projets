package Tests;

import Etat.EtatJeu;
import Etat.Joueur;
import Evaluations.Evaluation;
import Evaluations.EvaluationNaive;

public class TestEvaluationNaive {

    public static void main(String[] args) {

        EtatJeu e = new EtatJeu(8);

        Evaluation naive = new EvaluationNaive();

        int scoreBlanc = naive.eval(e, Joueur.joueurBlanc);

        int scoreNoir = naive.eval(e, Joueur.joueurNoir);

        System.out.println("Score Naive pour Blanc : " + scoreBlanc);
        System.out.println("Score Naive pour Noir : " + scoreNoir);
    }
}