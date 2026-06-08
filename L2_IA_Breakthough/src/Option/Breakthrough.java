package Option;
import Etat.Action;
import Etat.EtatJeu;
import Etat.Gagnant;
import Etat.Joueur;

import java.util.Scanner;

public class Breakthrough {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in); //pour enregistrer les choix des utilisateurs
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
            int ligne = sc.nextInt();
            System.out.println("Entrez la colonne du pion a déplacer : ");
            int colonne = sc.nextInt();
            System.out.println("Entrez la direction (-1, 0, 1) du pion a déplacer : ");
            int direction = sc.nextInt();
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
}