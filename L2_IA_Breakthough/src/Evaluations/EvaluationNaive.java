package Evaluations;

import Etat.EtatJeu;
import Etat.Joueur;

public class EvaluationNaive implements Evaluation {
    @Override
    public int eval(EtatJeu e, Joueur joueur) {

        int nbPionsJoueur = 0;  //pour compter le nombre de pions restant du joueur courant
        int nbPionsAdversaire = 0; //.... du joueur adverse

        for(int i = 0; i < 8; i++){
            for(int k = 0; k < e.getLargeur(); k++){
                Joueur contenu = e.getPlateau().getCase(i,k);
                if(contenu != null){ //on regarde chaque case et on incrémente si la case contient un pion
                    if(contenu.equals(joueur)){
                        nbPionsJoueur++; //du joueur
                    }
                    if(contenu.equals(joueur.autreJoueur())){
                        nbPionsAdversaire++; //ou de l'adversaire
                    }
                }
            }
        }
        return nbPionsJoueur - nbPionsAdversaire;
    }
}
