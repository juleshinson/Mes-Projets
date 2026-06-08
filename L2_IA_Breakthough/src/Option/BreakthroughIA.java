package Option;

import Etat.EtatJeu;
import Etat.Gagnant;
import Etat.IA;
import Etat.Joueur;

public class BreakthroughIA {
    public static void main(String[] args) {

        EtatJeu etatCourant = new EtatJeu(8);//On crée l'état initial du jeu

        int profondeur = 1;//On fixe la profondeur de recherche

        Gagnant gagnant = etatCourant.etatFinal();//On calcule l'état final actuel

        int numeroCoup = 1; //connaitre nombre de coup

        while(gagnant == Gagnant.AUCUN) {//Tant qu'il n'y a pas de gagnant, on continue la partie

            System.out.println("-----------------------------------");
            System.out.println("Coup numéro : " + numeroCoup);

            etatCourant.getPlateau().afficher(); //On affiche le plateau courant

            Joueur joueurCourant;
            EtatJeu nouvelEtat;
            if (etatCourant.getTourBlanc()) { //On détermine quel joueur doit jouer
                joueurCourant = Joueur.joueurBlanc;
                System.out.println("Le joueur qui joue est : " + joueurCourant); //On affiche le joueur qui va jouer
                nouvelEtat = IA.meilleurCoupMinimaxAvecEval0(etatCourant, joueurCourant, profondeur);//On demande à l'IA de choisir le meilleur coup
            } else {
                joueurCourant = Joueur.joueurNoir;
                System.out.println("Le joueur qui joue est : " + joueurCourant); //On affiche le joueur qui va jouer
                nouvelEtat = IA.meilleurCoupMinimaxAvecEval0(etatCourant, joueurCourant, profondeur);//On demande à l'IA de choisir le meilleur coup
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
}
