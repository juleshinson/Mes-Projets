package Evaluations;

import Etat.EtatJeu;
import Etat.Joueur;

public class EvaluationBooster implements Evaluation {

    public int eval(EtatJeu e, Joueur j) {

        int scoreJoueur = 0;
        int scoreAdversaire = 0;
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < e.getLargeur(); k++) {
                Joueur contenu = e.getPlateau().getCase(i, k);
                if (contenu != null) {
                    if (contenu == j) {
                        scoreJoueur = scoreJoueur + 10; //Un pion vaut déjà 10 points
                        /*On ajoute aussi un bonus d'avancement
                         Si j est Blanc, avancer vers le bas augmente i
                         Si j est Noir, avancer vers le haut signifie qu'un pion proche de la ligne 0 est bien placé.*/
                        if (j == Joueur.joueurBlanc) {
                            scoreJoueur = scoreJoueur + i;
                        } else {
                            scoreJoueur = scoreJoueur + (7 - i);
                        }
                    }
                    if (contenu == j.autreJoueur()) { //Si le pion appartient à l'adversaire
                        scoreAdversaire = scoreAdversaire + 10; //Même logique pour l'adversaire
                        if (j.autreJoueur() == Joueur.joueurBlanc) {
                            scoreAdversaire = scoreAdversaire + i;
                        } else {
                            scoreAdversaire = scoreAdversaire + (7 - i);
                        }
                    }
                }
            }
        }
        return scoreJoueur - scoreAdversaire; //On compare le score du joueur et celui de l'adversaire
    }
}
