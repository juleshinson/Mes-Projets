import Etat.*;
import Evaluations.Evaluation;
import Evaluations.EvaluationBooster;
import Evaluations.EvaluationMonteCarlo;
import Evaluations.EvaluationNaive;

import java.util.Scanner;

public class MainMenuBreakthrough {

    public static void main(String[] args) {

        Scanner clavier = new Scanner(System.in);
        int choixMode = 0; //pour entrer dans les boucles
        int choixEvaluation = 0;
        int profondeur;

        //On affiche le menu principal
        System.out.println("====================================");
        System.out.println("         BREAKTHROUGH - MENU        ");
        System.out.println("====================================");

        while(choixMode != 1 && choixMode != 2  && choixMode != 3) {
            System.out.println("Choisissez un mode de jeu :");
            System.out.println("1 - Humain contre Humain");
            System.out.println("2 - Humain contre IA");
            System.out.println("3 - IA contre IA");
            System.out.print("Votre choix : ");

            choixMode = clavier.nextInt();
            if(choixMode != 1 && choixMode != 2  && choixMode != 3) {
                System.out.println("Choix Invalide : Réessayer");
            }
        }

        if (choixMode == 2 || choixMode == 3) { //si un mode contenant une IA est choisie
            System.out.print("Entrez la profondeur de recherche de l'IA : ");
            profondeur = clavier.nextInt();
        } else {
            profondeur = 0;
        }

        //on met les évaluations par défaut à null
        Evaluation evaluationIA1 = null;
        Evaluation evaluationIA2 = null;

        //si un mode contenant une ia est choisie
        if (choixMode == 2 || choixMode == 3) {

            while(choixEvaluation != 1 && choixEvaluation != 2  && choixEvaluation != 3){
                System.out.println("------------------------------------");
                System.out.println("Choisissez le type d'évaluation de l'IA :");
                System.out.println("1 - Evaluation naive");
                System.out.println("2 - Evaluation booster");
                System.out.println("3 - Evaluation Monte-Carlo");
                System.out.print("Votre choix : ");

                choixEvaluation = clavier.nextInt();
                if(choixEvaluation != 1 && choixEvaluation != 2  && choixEvaluation != 3) {
                    System.out.println("Choix Invalide : Réessayer");
                }
            }


            if (choixEvaluation == 1) {
                evaluationIA1 = new EvaluationNaive();
            } else if (choixEvaluation == 2) {
                evaluationIA1 = new EvaluationBooster();
            } else {
                System.out.print("Nombre de simulations : ");
                int simulations = clavier.nextInt();
                evaluationIA1 = new EvaluationMonteCarlo(simulations, 200);
            }
            choixEvaluation = 0; //pour deuxièmes décisions
            //Si on est en IA contre IA, on demande aussi l'évaluation de la deuxième IA
            if (choixMode == 3) {
                while(choixEvaluation != 1 && choixEvaluation != 2  && choixEvaluation != 3){
                    System.out.println("------------------------------------");
                    System.out.println("Choisissez le type d'évaluation du second IA (Noir):");
                    System.out.println("1 - Evaluation naive");
                    System.out.println("2 - Evaluation booster");
                    System.out.println("3 - Evaluation Monte-Carlo");
                    System.out.print("Votre choix : ");

                    choixEvaluation = clavier.nextInt();
                    if(choixEvaluation != 1 && choixEvaluation != 2  && choixEvaluation != 3) {
                        System.out.println("Choix Invalide : Réessayer");
                    }
                }

                if (choixEvaluation == 1) {
                    evaluationIA2 = new EvaluationNaive();
                } else if (choixEvaluation == 2)  {
                    evaluationIA2 = new EvaluationBooster();
                }else {
                    System.out.print("Nombre de simulations : ");
                    int simulations = clavier.nextInt();
                    evaluationIA2 = new EvaluationMonteCarlo(simulations, 200);
                }
            }
        }
        //On lance le jeu
        if (choixMode == 1) {
            lancerHumainVsHumain(clavier);
        } else if (choixMode == 2) {
            lancerHumainVsIA(clavier, profondeur, evaluationIA1);
        } else {
            lancerIAVsIA(profondeur, evaluationIA1, evaluationIA2);
        }

        clavier.close();
    }

    public static void lancerHumainVsHumain(Scanner clavier) {

        EtatJeu e = new EtatJeu(8); //on crée le jeu

        Gagnant gagnant = e.etatFinal();  //on mémorise le gagnant

        while(gagnant == Gagnant.AUCUN){ //tant qu'il n'y a pas de gagnant on continue
            e.getPlateau().afficher(); //on affiche le plateau
            Joueur joueurCourant; //on met le joueur qui doit jouer
            if(e.getTourBlanc()){
                joueurCourant = Joueur.joueurBlanc;
            }else{
                joueurCourant = Joueur.joueurNoir;
            }
            //Consignes
            System.out.println("Au tour de " +joueurCourant + " de jouer");
            System.out.println("Entrez la ligne du pion a déplacer : ");
            int ligne = clavier.nextInt();
            System.out.println("Entrez la colonne du pion a déplacer : ");
            int colonne = clavier.nextInt();
            System.out.println("Entrez la direction (-1, 0, 1) du pion a déplacer : ");
            int direction = clavier.nextInt();
            Action a = new Action(ligne, colonne, direction);
            Boolean valide = e.actionValide(a, joueurCourant); //si l'action de l'utilisateur est valide
            if(valide){
                e = e.successeur(a, joueurCourant); //on passe a l'état successeur
            }else{
                System.out.println("Action invalide : Veuillez recommencer ."); //sinon on redemande une autre action
            }
            gagnant = e.etatFinal(); //on met a jour le gagnant
        }
        e.getPlateau().afficher(); //on réaffiche le plateau de fin
        System.out.println("Fin de la partie");
        System.out.println("Le " + gagnant + " est le vainqueur."); //on précise le vainqueur
    }

    public static void lancerHumainVsIA(Scanner clavier, int profondeur, Evaluation evaluationIA) {

        EtatJeu etatCourant = new EtatJeu(8);

        Joueur joueurHumain = Joueur.joueurBlanc;
        Joueur joueurIA = Joueur.joueurNoir;

        Gagnant gagnant = etatCourant.etatFinal();

        while (gagnant == Gagnant.AUCUN) { //Boucle principale du jeu : tant qu'il n'y a pas de gagnant, on continue

            System.out.println("-----------------------------------");

            etatCourant.getPlateau().afficher();

            Joueur joueurCourant;

            if (etatCourant.getTourBlanc()) {//qui doit jouer
                joueurCourant = Joueur.joueurBlanc;
            } else {
                joueurCourant = Joueur.joueurNoir;
            }

            System.out.println("Le joueur qui doit jouer est : " + joueurCourant); //On affiche le joueur courant
            //Cas 1 : c'est l'humain qui joue.
            if (joueurCourant == joueurHumain) {
                boolean actionTrouvee = false;

                while(!actionTrouvee){//On demandera une action tant qu'elle n'est pas valide
                    System.out.print("Entrez la ligne du pion a déplacer : ");
                    int ligne = clavier.nextInt();
                    System.out.print("Entrez la colonne du pion a déplacer : ");
                    int colonne = clavier.nextInt();
                    System.out.print("Entrez la direction (-1, 0 ou 1) : ");
                    int direction = clavier.nextInt();

                    Action actionChoisie = new Action(ligne, colonne, direction);

                    if (etatCourant.actionValide(actionChoisie, joueurHumain)) {
                        etatCourant = etatCourant.successeur(actionChoisie, joueurHumain);
                        actionTrouvee = true;
                    }else{
                        System.out.println("Action invalide. Veuillez recommencer.");
                    }
                }
            }else{//Cas 2 : c'est l'IA qui joue
                System.out.println("L'IA est en train de choisir un coup...");
                etatCourant = temps(etatCourant, joueurIA, profondeur, evaluationIA);//On demande à l'IA de choisir le meilleur coup en calculant le temps
            }
            gagnant = etatCourant.etatFinal();//Après le coup, on met à jour le gagnant éventuel
        }
        System.out.println("-----------------------------------");
        System.out.println("État final :");
        etatCourant.getPlateau().afficher();
        System.out.println("La partie est terminée.");
        System.out.println("Le gagnant est : " + gagnant);

    }

    public static void lancerIAVsIA(int profondeur, Evaluation evaluationIA1, Evaluation evaluationIA2) {

        EtatJeu etatCourant = new EtatJeu(8);//On crée l'état initial du jeu

        Gagnant gagnant = etatCourant.etatFinal();//On calcule l'état final actuel

        int numeroCoup = 1; //connaitre nombre de coups

        while(gagnant == Gagnant.AUCUN) {//Tant qu'il n'y a pas de gagnant, on continue la partie

            System.out.println("-----------------------------------");
            System.out.println("Coup numéro : " + numeroCoup);

            etatCourant.getPlateau().afficher(); //On affiche le plateau courant

            Joueur joueurCourant;
            EtatJeu nouvelEtat;
            if (etatCourant.getTourBlanc()) { //On détermine quel joueur doit jouer
                joueurCourant = Joueur.joueurBlanc;
                System.out.println("Le joueur qui joue est : " + joueurCourant); //On affiche le joueur qui va jouer
                nouvelEtat = temps(etatCourant, joueurCourant, profondeur, evaluationIA1);//On demande à l'IA de choisir le meilleur coup en calculant le temps
            } else {
                joueurCourant = Joueur.joueurNoir;
                System.out.println("Le joueur qui joue est : " + joueurCourant); //On affiche le joueur qui va jouer
                nouvelEtat = temps(etatCourant, joueurCourant, profondeur, evaluationIA2);//On demande à l'IA de choisir le meilleur coup en calculant le temps
            }

            if (nouvelEtat == etatCourant) {//Si l'état renvoyé est exactement le même objet
                System.out.println("Aucun nouveau coup n'a été trouvé.");
                if(etatCourant.getTourBlanc()) {
                    gagnant = Gagnant.NOIR;
                }else{
                    gagnant = Gagnant.BLANC;
                }
                break;// on arrête
            }
            etatCourant = nouvelEtat;

            gagnant = etatCourant.etatFinal();//On met à jour l'état final
            numeroCoup = numeroCoup + 1;
        }
        System.out.println("-----------------------------------");
        System.out.println("État final :");
        etatCourant.getPlateau().afficher();

        System.out.println("La partie est terminée.");// On annonce le résultat final
        System.out.println("Gagnant : " + gagnant);
    }

    public static EtatJeu temps(EtatJeu etatCourant, Joueur joueurCourant, int profondeur, Evaluation evaluationIA) {
        long debut = System.nanoTime();//on calcule le temps
        EtatJeu etatJeu = IA.meilleurCoupAlphaBetaAvecEval(etatCourant, joueurCourant, profondeur, evaluationIA);//On demande à l'IA de choisir le meilleur coup;
        long fin = System.nanoTime(); //On enregistre l'instant de fin en nanosecondes
        long dureeNano = fin - debut; //On calcule la durée totale en nanosecondes
        double dureeMilli = dureeNano / 1000000.0; //On convertit aussi en millisecondes
        System.out.println("Temps en nanosecondes : " + dureeNano);
        System.out.println("Temps en millisecondes : " + dureeMilli);
        return etatJeu;
    }
}