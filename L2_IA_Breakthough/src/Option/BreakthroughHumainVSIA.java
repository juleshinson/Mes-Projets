package Option;
import Etat.*;

import java.util.Scanner;

public class BreakthroughHumainVSIA {
    public static void main(String[] args) {

        Scanner clavier = new Scanner(System.in);
        EtatJeu etatCourant = new EtatJeu(8);

        int profondeur = 1;

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
                etatCourant = IA.meilleurCoupMinimaxAvecEval0(etatCourant, joueurIA, profondeur);//On demande à l'IA de choisir le meilleur coup
            }
            gagnant = etatCourant.etatFinal();//Après le coup, on met à jour le gagnant éventuel
        }
        System.out.println("-----------------------------------");
        System.out.println("Etat final :");
        etatCourant.getPlateau().afficher();
        System.out.println("La partie est terminée.");
        System.out.println("Le gagnant est : " + gagnant);
        clavier.close();
    }
}
