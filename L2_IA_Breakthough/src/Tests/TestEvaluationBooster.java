package Tests;

import Etat.EtatJeu;
import Etat.Joueur;
import Evaluations.Evaluation;
import Evaluations.EvaluationBooster;

public class TestEvaluationBooster {

    public static void main(String[] args) {

        EtatJeu e = new EtatJeu(8);

        Evaluation booster = new EvaluationBooster();

        int scoreBlanc = booster.eval(e, Joueur.joueurBlanc);

        int scoreNoir = booster.eval(e, Joueur.joueurNoir);

        System.out.println("Score Booster pour Blanc : " + scoreBlanc);
        System.out.println("Score Booster pour Noir : " + scoreNoir);
    }
}