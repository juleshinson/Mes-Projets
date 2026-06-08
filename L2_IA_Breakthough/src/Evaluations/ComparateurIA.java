package Evaluations;

import Etat.EtatJeu;
import Etat.Gagnant;
import Etat.IA;
import Etat.Joueur;

public class ComparateurIA {

    public static Gagnant jouerPartie(Evaluation evalBlanc, Evaluation evalNoir, int profondeur, int limiteCoups) {

        EtatJeu etatCourant = new EtatJeu(8);

        Gagnant gagnant = etatCourant.etatFinal(); //On regarde si la partie est déjà terminée

        int numeroCoup = 1; //Compteur de coups pour éviter une boucle infinie

        while (gagnant == Gagnant.AUCUN && numeroCoup <= limiteCoups) {

            Joueur joueurCourant;

            if (etatCourant.getTourBlanc()) {
                joueurCourant = Joueur.joueurBlanc;
            } else {
                joueurCourant = Joueur.joueurNoir;
            }

            EtatJeu nouvelEtat;

            if (joueurCourant == Joueur.joueurBlanc) {//Si Blanc joue, on utilise evalBlanc
                nouvelEtat = IA.meilleurCoupAlphaBetaAvecEval(etatCourant, Joueur.joueurBlanc, profondeur, evalBlanc);
            } else { //Si Noir joue, on utilise evalNoir
                nouvelEtat = IA.meilleurCoupAlphaBetaAvecEval(etatCourant,Joueur.joueurNoir, profondeur, evalNoir);
            }
            if (nouvelEtat == etatCourant) { //Si aucun nouveau coup n'est trouvé, le joueur courant perd
                if (joueurCourant == Joueur.joueurBlanc) {
                    gagnant = Gagnant.NOIR;
                } else {
                    gagnant = Gagnant.BLANC;
                }
                break;
            }
            etatCourant = nouvelEtat; //On met à jour l'état courant

            gagnant = etatCourant.etatFinal(); //On met à jour le gagnant

            numeroCoup = numeroCoup + 1; //On augmente le nombre de coups
        }
        return gagnant; //Si on a dépassé la limite sans gagnant, on renvoie AUCUN
    }

    public static int comparer(Evaluation evalA, Evaluation evalB, int profondeur, int limiteCoups) {

        int scoreA = 0;
        Gagnant resultat1 = jouerPartie(evalA, evalB, profondeur, limiteCoups);
        if (resultat1 == Gagnant.BLANC) {
            scoreA = scoreA + 1;
        }
        Gagnant resultat2 = jouerPartie(evalB, evalA, profondeur, limiteCoups);
        if (resultat2 == Gagnant.NOIR) {
            scoreA = scoreA + 1;
        }
        /*
         On interprète le score final.
         2 : A semble meilleure
         1 : égalité approximative
         0 : B semble meilleure
         */
        return scoreA;
    }
}
