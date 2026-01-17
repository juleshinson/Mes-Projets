package Vue;

import modele.Coup;
import modele.Jeu;
import modele.carte.Couleur;

// L'interface VueJeu représente la "vue" du jeu
// Elle sert à séparer la logique du jeu de l'affichage.
// La vue est en console
public interface VueJeu {

    // Affiche le joueur dont c'est le tour actuellement (joueur courant)
    public void afficherJoueurCourant(Jeu jeu);

    // Affiche l'état complet du jeu (joueurs, expéditions, main, défausses, pioche...)
    public void afficher(Jeu jeu);

    // Affiche les scores des joueurs
    public void afficherScores(Jeu jeu);

    // Demande à l'utilisateur quel coup il veut faire :
    // - quel indice de carte
    // - et si c'est jouer ou défausser
    public Coup demanderCoup();

    // Demande à l'utilisateur d'où il veut piocher :
    // - pioche principale
    // - ou bien une défausse (retourne une Couleur)
    public Couleur demanderPioche();
}

