package modele;

import modele.carte.Couleur;
import modele.carte.Expedition;
import modele.carte.Main;
import modele.strategie.Strategie;

// La classe Joueur représente un joueur dans la partie.
// Un joueur a :
// - un nom (pour l'affichage)
// - une main (les cartes qu'il possède en main)
// - des expéditions (une par couleur) où il va poser ses cartes
// - une stratégie (humain ou bot) qui décide quel coup jouer et où piocher
public class Joueur {

    // Nom du joueur
    // Il est final car le nom ne change pas une fois le joueur créé
    private final String nom;

    // La main du joueur : les cartes qu'il tient actuellement
    // Elle est final car la main existe tout le temps (on ne la remplace jamais)
    private final Main main;

    // Tableau des expéditions du joueur
    // Il y en a 5, une pour chaque couleur
    private Expedition[] expeditions;

    // La stratégie du joueur
    // Elle peut être une stratégie "humaine"
    // ou une stratégie "bot"
    private Strategie strategie;

    // Constructeur du joueur
    // On donne un nom au joueur, on crée sa main vide,
    // et on initialise ses 5 expéditions (une par couleur)
    public Joueur(String nom) {
        this.nom = nom;
        this.main = new Main();
        initExpeditions();
    }

    // Méthode privée qui initialise les expéditions du joueur
    // On crée 5 expéditions, une par couleur
    private void initExpeditions() {
        this.expeditions = new Expedition[5];
        expeditions[0] = new Expedition(Couleur.JAUNE);
        expeditions[1] = new Expedition(Couleur.BLANC);
        expeditions[2] = new Expedition(Couleur.BLEU);
        expeditions[3] = new Expedition(Couleur.VERT);
        expeditions[4] = new Expedition(Couleur.ROUGE);
    }

    // Retourne le nom du joueur (utile pour l'affichage)
    public String getNom() {
        return nom;
    }

    // Retourne la main du joueur
    // Cela permet au moteur du jeu de voir/retirer/ajouter des cartes dans la main
    public Main getMain() {
        return main;
    }

    // Retourne l'expédition du joueur correspondant à une couleur donnée
    public Expedition getExpedition(Couleur couleur) {
        for (int i = 0; i < 5; i++) {
            if (expeditions[i].getCouleur() == couleur) {
                return expeditions[i];
            }
        }
        // Normalement ça n'arrive pas si toutes les couleurs existent bien,
        // mais on retourne null si aucune expédition ne correspond
        return null;
    }

    // Demande à la stratégie du joueur de choisir un coup à jouer
    // Le joueur lui-même ne décide pas : c'est la stratégie (humain/bot) qui décide
    public Coup choisirCoup(Jeu jeu) {
        return strategie.choisirCoup(jeu);
    }

    // Demande à la stratégie du joueur de choisir d'où piocher
    // null = pioche principale, sinon une couleur = piocher dans la défausse de cette couleur
    public Couleur choisirPioche(Jeu jeu) {
        return strategie.choisirPioche(jeu);
    }

    // Permet d'affecter une stratégie au joueur
    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }

    // Calcule le score total du joueur
    // Le score final est la somme des scores de ses 5 expéditions
    public int getScore() {
        int total = 0;

        // Pour chaque couleur, on calcule le score de l'expédition correspondante
        // et on l'ajoute au total
        for (Couleur couleur : Couleur.values()) {
            total += getExpedition(couleur).calculerScore();
        }

        return total;
    }
}
