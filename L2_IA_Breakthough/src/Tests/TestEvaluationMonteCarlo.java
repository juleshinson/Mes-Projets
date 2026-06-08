package Tests;

import Etat.EtatJeu;
import Etat.Joueur;
import Evaluations.Evaluation;
import Evaluations.EvaluationMonteCarlo;

public class TestEvaluationMonteCarlo {

    public static void main(String[] args) {

        EtatJeu e = new EtatJeu(8);

        // Diagnostic : 20 parties manuelles pour voir combien ont un gagnant
        java.util.Random hasard = new java.util.Random();
        int aucun = 0, blanc = 0, noir = 0;

        for (int i = 0; i < 20; i++) {
            EtatJeu copie = e.copie(e.getLargeur());
            int coups = 0;
            Etat.Gagnant gagnant = copie.etatFinal();

            while (gagnant == Etat.Gagnant.AUCUN && coups < 150) {
                Joueur joueurCourant = copie.getTourBlanc() ? Joueur.joueurBlanc : Joueur.joueurNoir;
                java.util.List<EtatJeu> successeurs = copie.successeurs(joueurCourant);
                if (successeurs.isEmpty()) break;
                copie = successeurs.get(hasard.nextInt(successeurs.size()));
                gagnant = copie.etatFinal();
                coups++;
            }

            System.out.println("Partie " + (i+1) + " : " + coups + " coups -> " + gagnant);
            if (gagnant == Etat.Gagnant.BLANC) blanc++;
            else if (gagnant == Etat.Gagnant.NOIR) noir++;
            else aucun++;
        }

        System.out.println("\nBlanc=" + blanc + " Noir=" + noir + " Aucun(sans gagnant)=" + aucun);
    }
}