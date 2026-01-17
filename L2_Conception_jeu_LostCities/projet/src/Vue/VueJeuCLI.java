package Vue;

import modele.Coup;
import modele.Jeu;
import modele.Joueur;
import modele.TypeCoup;
import modele.carte.Couleur;

import java.util.*;
import java.util.Scanner;

// La classe VueJeuCLI est une implémentation de VueJeu en mode console.
public class VueJeuCLI implements VueJeu {

    // Référence vers le jeu
    // Cela permet à la vue d'accéder à l'état actuel (joueur courant, main, défausses, etc.)
    private final Jeu jeu;

    // Constructeur : on donne la référence du jeu à la vue
    public VueJeuCLI(Jeu jeu) {
        this.jeu = jeu;
    }

    // Affiche un petit titre avec le nom du joueur courant
    @Override
    public void afficherJoueurCourant(Jeu jeu) {
        System.out.println("\nMain de " + jeu.getJoueurCourant().getNom() + " :");
    }

    // Affiche l'état du jeu (version console)
    @Override
    public void afficher(Jeu jeu) {

        // Titre général
        System.out.println("===== ÉTAT DU JEU =====");

        // On affiche d'abord les expéditions de chaque joueur
        for (int j = 0; j < 2; j++) {

            // On récupère le joueur j (0 ou 1)
            Joueur joueur = jeu.getJoueur(j);

            // On affiche son nom
            System.out.println(joueur.getNom());

            // Pour chaque couleur, on affiche l'expédition correspondante
            for (Couleur c : Couleur.values()) {
                System.out.println("  " + c + " : " + joueur.getExpedition(c));
            }
        }

        // On récupère ensuite le joueur courant pour afficher sa main
        Joueur courant = jeu.getJoueurCourant();

        // Affichage du titre "Main de ..."
        afficherJoueurCourant(jeu);

        // On affiche toutes les cartes de la main avec leurs indices
        // Les indices sont importants car l'utilisateur doit choisir un indice ensuite
        for (int i = 0; i < courant.getMain().getNbCartes(); i++) {
            System.out.println(i + " -> " + courant.getMain().voirCarte(i));
        }

        // Affichage des défausses
        System.out.println("\nDéfausses :");
        for (Couleur c : Couleur.values()) {
            System.out.println(c + " : " + jeu.getDefausse(c));
        }

        // Affichage du nombre de cartes restantes dans la pioche
        // Ça permet de savoir quand la partie est bientôt terminée
        System.out.println("\nCartes restantes dans la pioche : " + jeu.getPioche().getNbCartes());
    }

    // Affiche les scores des deux joueurs
    @Override
    public void afficherScores(Jeu jeu) {
        System.out.println("===== SCORES =====");

        // Comme il y a deux joueurs, on les affiche tous les deux
        for (int i = 0; i < 2; i++) {
            Joueur j = jeu.getJoueur(i);
            System.out.println(j.getNom() + " : " + j.getScore());
        }
    }

    // Demande à l'utilisateur le coup qu'il veut faire :
    // - l'indice de la carte dans la main
    // - le type : jouer ou défausser
    @Override
    public Coup demanderCoup() {

        // Scanner pour lire au clavier
        Scanner sc = new Scanner(System.in);

        // On demande l'indice de la carte à utiliser
        System.out.print("Indice de carte à jouer/défausser : ");

        // On lit un entier entre 0 et nbCartes-1
        // Comme ça, on évite les indices invalides
        int indice = lireIntEntre(sc, 0, jeu.getJoueurCourant().getMain().getNbCartes() - 1);

        // On demande le type de coup
        System.out.print("Type (0 = JOUER, 1 = DEFAUSSER) : ");

        // On lit un entier entre 0 et 1
        int type = lireIntEntre(sc, 0, 1);

        // On transforme le 0/1 en TypeCoup
        TypeCoup t = (type == 0) ? TypeCoup.JOUER : TypeCoup.DEFAUSSER;

        // On crée le coup correspondant et on le renvoie au contrôleur / au jeu
        return new Coup(indice, t);
    }

    // Demande à l'utilisateur où il veut piocher :
    // - 0 => pioche principale (on retourne null)
    // - 1 => défausse (on retourne une couleur)
    @Override
    public Couleur demanderPioche() {

        // Scanner pour lire au clavier
        Scanner sc = new Scanner(System.in);

        // On demande le choix de pioche
        System.out.print("Pioche (0 = principale, 1 = défausse) : ");

        // Lecture du choix entre 0 et 1
        int choix = lireIntEntre(sc, 0, 1);

        // Si l'utilisateur choisit 0, ça veut dire pioche principale
        if (choix == 0) {
            return null;
        }

        // Sinon, on demande la couleur de la défausse
        System.out.print("Couleur (JAUNE, BLANC, BLEU, VERT, ROUGE) : ");

        // On lit la couleur en texte et on la convertit en enum Couleur
        return lireCouleur(sc);   // pioche défausse couleur
    }

    // Méthode utilitaire pour lire un entier dans un intervalle [min, max]
    // Elle évite que le programme s'arrête si l'utilisateur tape n'importe quoi.
    private int lireIntEntre(Scanner sc, int min, int max) {
        while (true) {
            try {
                // Lecture d'un entier
                int v = sc.nextInt();

                // On consomme la fin de ligne
                sc.nextLine();

                // Vérification de l'intervalle
                if (v < min || v > max) {
                    System.out.println("Valeur invalide (Entrez entre " + min + " à " + max + "). Recommence.");
                    continue;
                }

                // Si tout est bon, on retourne la valeur
                return v;

            } catch (InputMismatchException e) {
                // Cas où l'utilisateur n'a pas tapé un nombre
                System.out.println("Mauvais type. Recommence.");

                // On jette l'entrée invalide pour pouvoir relire proprement ensuite
                sc.nextLine();
            }
        }
    }

    // Méthode utilitaire pour lire une couleur au clavier
    // L'utilisateur doit taper une des valeurs de l'enum Couleur
    // Exemple : "BLANC", "ROUGE", etc.
    // Si l'utilisateur se trompe, on affiche un message et on recommence.
    private Couleur lireCouleur(Scanner sc) {
        while (true) {

            // On lit la ligne, on enlève les espaces autour et on met en majuscules
            // Comme ça, "blanc" et " BLANC " fonctionneront aussi
            String s = sc.nextLine().trim().toUpperCase();

            try {
                // Conversion du texte en valeur de l'enum
                return Couleur.valueOf(s);

            } catch (IllegalArgumentException e) {
                // Si l'utilisateur tape une couleur qui n'existe pas dans l'enum
                System.out.println("Couleur invalide. Tape: JAUNE, BLANC, BLEU, VERT, ROUGE");
            }
        }
    }
}
