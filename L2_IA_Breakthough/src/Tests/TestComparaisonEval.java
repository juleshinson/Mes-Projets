package Tests;

import Evaluations.Evaluation;
import Evaluations.EvaluationBooster;
import Evaluations.EvaluationMonteCarlo;
import Evaluations.EvaluationNaive;
import Evaluations.ComparateurIA;

public class TestComparaisonEval {

    public static void main(String[] args) {

        /*int profondeur = 2;
        int limitePartie = 200;
        Evaluation naive = new EvaluationNaive();
        Evaluation booster = new EvaluationBooster();

        int[] simulations = {50, 100, 200};
        int[] limitesSimulation = {50, 80, 150};

        for (int sim : simulations) {
            for (int limite : limitesSimulation) {
                Evaluation mc = new EvaluationMonteCarlo(sim, limite);

                int r1 = ComparateurIA.comparer(mc, naive, profondeur, limitePartie);
                int r2 = ComparateurIA.comparer(mc, booster, profondeur, limitePartie);

                System.out.println("simulations=" + sim + " limiteCoups=" + limite + " | vs Naive: " + r1 + " | vs Booster: " + r2);
            }
        }*/
        int limitePartie = 200;
        Evaluation naive = new EvaluationNaive();
        Evaluation booster = new EvaluationBooster();

        System.out.println("=== Booster vs Naive (score 2=Booster gagne, 1=egal, 0=Naive gagne) ===\n");

        // Test 1 : profondeur 1
        System.out.println("--- Profondeur 1 ---");
        int r1 = ComparateurIA.comparer(booster, naive, 1, limitePartie);
        afficherResultat("Booster", "Naive", r1);

        // Test 2 : profondeur 2
        System.out.println("--- Profondeur 2 ---");
        int r2 = ComparateurIA.comparer(booster, naive, 2, limitePartie);
        afficherResultat("Booster", "Naive", r2);

        // Test 3 : profondeur 3
        System.out.println("--- Profondeur 3 ---");
        int r3 = ComparateurIA.comparer(booster, naive, 3, limitePartie);
        afficherResultat("Booster", "Naive", r3);

        // Test 4 : Naive vs Booster (roles inverses, pour verifier la coherence)
        System.out.println("--- Profondeur 2 (roles inverses : Naive=A, Booster=B) ---");
        int r4 = ComparateurIA.comparer(naive, booster, 2, limitePartie);
        afficherResultat("Naive", "Booster", r4);

        // Résumé
        System.out.println("\n=== Resume ===");
        System.out.println("Profondeur 1 : Booster=" + r1 + "/2");
        System.out.println("Profondeur 2 : Booster=" + r2 + "/2");
        System.out.println("Profondeur 3 : Booster=" + r3 + "/2");
        System.out.println("Coherence    : Naive score=" + r4 + "/2 (doit etre 0 si Booster > Naive)");
    }

    private static void afficherResultat(String nomA, String nomB, int score) {
        if (score == 2) {
            System.out.println("  Resultat : " + nomA + " gagne les 2 parties -> " + nomA + " > " + nomB);
        } else if (score == 1) {
            System.out.println("  Resultat : 1 victoire chacun -> egalite approximative");
        } else {
            System.out.println("  Resultat : " + nomB + " gagne les 2 parties -> " + nomB + " > " + nomA);
        }
    }
}


