import Vue.*;
import controle.ControleJeu;
import modele.Jeu;
import Vue.VueJeu;
import modele.strategie.*;
import java.util.*;

// La classe LostCities contient le point d'entrée du programme (main).
// Son rôle est de lancer le jeu, demander le mode de jeu à l'utilisateur,
// configurer les stratégies des joueurs (humain ou bots),
// puis lancer la partie via le contrôleur.
public class LostCities {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choisir le mode de jeu :");
        System.out.println("1 - BotMin vs BotDefausse");
        System.out.println("2 - BotMin vs BotMinAmeliore");
        System.out.println("3 - Humain vs BotMin");
        System.out.println("4 - Humain vs BotDefausse");
        System.out.println("5 - Humain vs BotMinAmeliore");
        System.out.println("6 - Humain vs Humain");
        System.out.print("Votre choix : ");
        int choix = lireIntEntre(sc, 1, 6);
        // ===== Création du jeu =====
        Jeu jeu;
        if (choix == 3 || choix == 4 || choix == 5) {
            System.out.print("Entrez votre nom : ");
            String nom = sc.nextLine().trim();
            jeu = new Jeu(nom);
        }
        else if (choix == 6) {
            System.out.print("Entrez le nom du joueur 1 : ");
            String nom1 = sc.nextLine().trim();
            System.out.print("Entrez le nom du joueur 2 : ");
            String nom2 = sc.nextLine().trim();
            jeu = new Jeu(nom1, nom2);
        }
        else {
            jeu = new Jeu(); // bot vs bot
        }
        //Création de la vue
        VueJeu vue = new VueJeuCLI(jeu);
        // Attribution des stratégies
        if (choix == 1) {
            System.out.print("Joueur 1 = BotMin et Joueur 2 = BotDefausse :\n ");
            jeu.getJoueur(0).setStrategie(new BotMin());
            jeu.getJoueur(1).setStrategie(new BotDefausse());
        }
        else if (choix == 2) {
            System.out.print("Joueur 1 = BotMin et Joueur 2 = BotMinAmeliore :\n ");
            jeu.getJoueur(0).setStrategie(new BotMin());
            jeu.getJoueur(1).setStrategie(new BotMinAmeliore());
        }
        else if (choix == 3) {
            jeu.getJoueur(0).setStrategie(new Humain(vue));
            jeu.getJoueur(1).setStrategie(new BotMin());
        }
        else if (choix == 4) {
            jeu.getJoueur(0).setStrategie(new Humain(vue));
            jeu.getJoueur(1).setStrategie(new BotDefausse());
        }
        else if (choix == 5) {
            jeu.getJoueur(0).setStrategie(new Humain(vue));
            jeu.getJoueur(1).setStrategie(new BotMinAmeliore());
        }
        else if (choix == 6) {
            jeu.getJoueur(0).setStrategie(new Humain(vue));
            jeu.getJoueur(1).setStrategie(new Humain(vue));
        }
        // Lancement du jeu
        ControleJeu controle = new ControleJeu(jeu, vue);
        controle.lancerJeu();
    }
    private static int lireIntEntre(Scanner sc, int min, int max) {
        while (true) {
            try {
                int v = sc.nextInt();
                sc.nextLine();
                if (v < min || v > max) {
                    System.out.println("Valeur invalide (Entrez entre " + min + " à " + max + "). Recommence.");
                    continue;
                }
                return v;
            } catch (InputMismatchException e) {
                System.out.println("Mauvais type. Recommence.");
                sc.nextLine();
            }
        }
    }
}
