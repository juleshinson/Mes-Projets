package Evaluations;
import Etat.EtatJeu;
import Etat.Gagnant;
import Etat.Joueur;

import java.util.List;
import java.util.Random;

public class EvaluationMonteCarlo implements Evaluation {

    private final int nombreSimulations;
    private final int limiteCoups;
    private final Random hasard;

    public EvaluationMonteCarlo(int nombreSimulations, int limiteCoups) {
        this.nombreSimulations = nombreSimulations;
        this.limiteCoups = limiteCoups;
        this.hasard = new Random();
    }

    public int eval(EtatJeu e, Joueur j) {

        int victoires = 0;

        for (int i = 0; i < nombreSimulations; i++) {
            EtatJeu copie = e.copie(e.getLargeur());
            int coups = 0;

            Gagnant gagnant = copie.etatFinal();

            while (gagnant == Gagnant.AUCUN && coups < limiteCoups) {
                Joueur joueurCourant ;
                if(copie.getTourBlanc()){
                    joueurCourant = Joueur.joueurBlanc;
                }else {
                    joueurCourant = Joueur.joueurNoir;
                }
                List<EtatJeu> successeurs = copie.successeurs(joueurCourant);

                if (successeurs.isEmpty()) {
                    break;
                }
                // Choix aléatoire d'un coup
                int index = hasard.nextInt(successeurs.size());
                copie = successeurs.get(index);
                gagnant = copie.etatFinal();
                coups++;
            }

            if (gagnant == Gagnant.BLANC && j == Joueur.joueurBlanc) {
                victoires++;
            }

            if (gagnant == Gagnant.NOIR && j == Joueur.joueurNoir) {
                victoires++;
            }
        }

        double p = (double) victoires / nombreSimulations;

        double valeur = 2 * p - 1;

        return (int)(valeur * 100); // on convertit en int pour Minimax
    }

    public String toString() {
        return "EvaluationMonteCarlo(simulations=" + nombreSimulations + ")";
    }
}