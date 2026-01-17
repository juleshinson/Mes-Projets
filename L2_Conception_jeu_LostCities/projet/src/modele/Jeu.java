package modele;

import modele.carte.*;

import java.util.ArrayList;
import java.util.List;

// La classe Jeu représente le moteur principal du jeu.
// Elle contient toute la logique : joueurs, pioche, défausses, tours de jeu, règles pour jouer une carte, piocher, et déterminer la fin de la partie.
public class Jeu {

    // Indique l'indice du joueur courant (0 ou 1)
    private int joueurCourant;

    // La pioche principale du jeu
    private Pioche pioche = new Pioche();

    // Tableau des défausses, une par couleur
    private Defausse[] defausses;

    // Tableau contenant les deux joueurs
    private Joueur[] joueurs;

    // Mémorise le dernier joueur qui a défaussé une carte
    // Sert à empêcher un joueur de reprendre immédiatement sa propre défausse
    private Joueur dernierjoueur;

    // Constructeur principal du jeu
    // Il initialise complètement une partie avec deux joueurs
    // Les noms des joueurs sont passés en paramètres
    public Jeu(String nom1, String nom2) {

        // Initialisation de la pioche avec toutes les cartes du jeu
        pioche.init(initcartes());

        // Initialisation des défausses (une par couleur)
        initDefausse();

        // Création des deux joueurs
        joueurs = new Joueur[2];
        joueurs[0] = new Joueur(nom1);
        joueurs[1] = new Joueur(nom2);

        // Distribution initiale : chaque joueur reçoit 8 cartes
        for (int i = 0; i < 8; i++) {
            Carte c = getPioche().prendreCarte();
            joueurs[0].getMain().ajouterCartes(c);

            c = getPioche().prendreCarte();
            joueurs[1].getMain().ajouterCartes(c);
        }

        // Le premier joueur commence la partie
        joueurCourant = 0;
    }

    // Constructeur par défaut
    // Crée une partie avec deux joueurs nommés "Joueur 1" et "Joueur 2"
    public Jeu() {
        this("Joueur 1", "Joueur 2");
    }

    // Constructeur avec un seul nom
    // Crée une partie entre un joueur humain et un bot
    public Jeu(String nom) {
        this(nom, "BOT");
    }

    // Méthode privée qui crée toutes les cartes du jeu. Elle génère les cartes pour chaque couleur
    private List<Carte> initcartes() {
        List<Carte> cartes = new ArrayList<>();

        // Pour chaque couleur du jeu
        for (Couleur c : Couleur.values()) {

            // Ajout des 3 cartes PARI
            cartes.add(new Carte(c, TypeCarte.PARI));
            cartes.add(new Carte(c, TypeCarte.PARI));
            cartes.add(new Carte(c, TypeCarte.PARI));

            // Ajout des 9 cartes chiffrées de 2 à 10
            cartes.add(new Carte(c, TypeCarte.DEUX));
            cartes.add(new Carte(c, TypeCarte.TROIS));
            cartes.add(new Carte(c, TypeCarte.QUATRE));
            cartes.add(new Carte(c, TypeCarte.CINQ));
            cartes.add(new Carte(c, TypeCarte.SIX));
            cartes.add(new Carte(c, TypeCarte.SEPT));
            cartes.add(new Carte(c, TypeCarte.HUIT));
            cartes.add(new Carte(c, TypeCarte.NEUF));
            cartes.add(new Carte(c, TypeCarte.DIX));
        }
        return cartes;
    }

    // Méthode privée qui initialise les défausses
    // Une défausse est créée pour chaque couleur
    private void initDefausse() {
        this.defausses = new Defausse[5];
        defausses[0] = new Defausse(Couleur.JAUNE);
        defausses[1] = new Defausse(Couleur.BLANC);
        defausses[2] = new Defausse(Couleur.BLEU);
        defausses[3] = new Defausse(Couleur.VERT);
        defausses[4] = new Defausse(Couleur.ROUGE);
    }

    // Méthode qui permet au joueur courant de jouer un coup
    // Le coup peut être soit JOUER une carte, soit DEFAUSSER une carte
    public boolean jouer(Coup coup) {

        // On récupère la carte concernée depuis la main du joueur courant
        Carte c = getJoueurCourant().getMain().voirCarte(coup.getIndice());

        // Cas où le joueur veut jouer la carte sur une expédition
        if (coup.getType() == TypeCoup.JOUER) {

            // On vérifie que la carte peut être posée selon les règles
            if (getJoueurCourant().getExpedition(c.getCouleur()).peutPoser(c)) {

                // On retire la carte de la main
                c = getJoueurCourant().getMain().prendreCarte(coup.getIndice());

                // On pose la carte sur l'expédition correspondante
                return getJoueurCourant().getExpedition(c.getCouleur()).poser(c);
            } else {
                // Coup invalide
                return false;
            }

            // Cas où le joueur veut défausser la carte
        } else if (coup.getType() == TypeCoup.DEFAUSSER) {

            // On retire la carte de la main
            c = getJoueurCourant().getMain().prendreCarte(coup.getIndice());

            // On mémorise le joueur qui vient de défausser
            setDernierjoueur(getJoueurCourant());

            // On pose la carte sur la défausse correspondante
            return getDefausse(c.getCouleur()).poser(c);
        }

        // Type de coup inconnu
        return false;
    }

    // Méthode qui permet au joueur courant de piocher une carte
    // La pioche peut être la pioche principale  ou une défausse d'une couleur donnée
    public boolean piocher(Couleur couleur) {
        Carte c;

        // Cas de la pioche principale
        if (couleur == null) {

            // Si la pioche est vide, la partie est terminée
            if (estFini()) {
                return false;
            }

            // On prend une carte depuis la pioche principale
            c = getPioche().prendreCarte();

        } else {
            // Cas de la pioche dans une défausse
            Defausse d = getDefausse(couleur);

            // Si la défausse est vide, on ne peut pas piocher
            if (d.estVide()) {
                return false;

                // Un joueur ne peut pas reprendre sa propre défausse
            } else if (getDernierjoueur() == getJoueurCourant()) {
                return false;
            }

            // On prend la carte depuis la défausse
            c = d.prendreCarte();
        }

        // La carte piochée est ajoutée à la main du joueur courant
        getJoueurCourant().getMain().ajouterCartes(c);
        return true;
    }

    // Méthode privée qui retourne le dernier joueur ayant défaussé
    private Joueur getDernierjoueur() {
        return dernierjoueur;
    }

    // Méthode qui permet de définir le dernier joueur ayant défaussé
    public void setDernierjoueur(Joueur dernierjoueur) {
        this.dernierjoueur = dernierjoueur;
    }

    // Méthode qui indique si la partie est terminée
    // La partie se termine quand la pioche principale est vide
    public boolean estFini() {
        return getPioche().estVide();
    }

    // Retourne un joueur à partir de son indice (0 ou 1)
    public Joueur getJoueur(int indice) {
        return joueurs[indice];
    }

    // Retourne le joueur dont c'est le tour actuellement
    public Joueur getJoueurCourant() {
        return joueurs[joueurCourant];
    }

    // Passe au joueur suivant
    // Comme il n'y a que deux joueurs, on inverse simplement l'indice
    public void passerJoueurSuivant() {
        joueurCourant = 1 - joueurCourant;
    }

    // Retourne la pioche principale
    public Pioche getPioche() {
        return pioche;
    }

    // Retourne la défausse correspondant à une couleur donnée
    public Defausse getDefausse(Couleur couleur) {
        for (int i = 0; i < 5; i++) {
            if (defausses[i].getCouleur() == couleur) {
                return defausses[i];
            }
        }
        return null;
    }
}
