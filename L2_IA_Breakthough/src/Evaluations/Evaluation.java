package Evaluations;

import Etat.EtatJeu;
import Etat.Joueur;

public interface Evaluation {
    int eval(EtatJeu e, Joueur joueur);
}
