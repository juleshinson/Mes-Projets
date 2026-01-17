package controle;

import Vue.VueJeu;
import modele.Coup;
import modele.Jeu;
import modele.Joueur;
import modele.carte.Couleur;

public class ControleJeu {
    private Jeu jeu;
    private VueJeu vue;

    public ControleJeu(Jeu jeu, VueJeu vue) {
        this.jeu = jeu;
        this.vue = vue;
    }

    public void lancerJeu() {

        // La partie continue tant que la pioche n'est pas vide
        while (!jeu.estFini()) {

            Joueur joueurCourant = jeu.getJoueurCourant();

            boolean coupValide = false;

            //PHASE 1 : JOUER OU DÉFAUSSER =====
            // Tant que le coup n'est pas valide, on redemande
            while (!coupValide) {
                Coup coup = joueurCourant.choisirCoup(jeu);
                coupValide = jeu.jouer(coup);

                if (!coupValide) {
                    System.out.println("Coup invalide. Recommence.");
                }
            }

            boolean piocheValide = false;

            // PHASE 2 : PIOCHER
            // Tant que la pioche n'est pas valide, on redemande
            while (!piocheValide) {
                Couleur pioche = joueurCourant.choisirPioche(jeu);
                piocheValide = jeu.piocher(pioche);

                jeu.setDernierjoueur(null);

                if (!piocheValide) {
                    System.out.println("Pioche impossible. Recommence.");
                }
            }

            //FIN DU TOUR
            // On passe seulement maintenant au joueur suivant
            jeu.passerJoueurSuivant();
        }

        //FIN DE PARTIE
        // Quand la pioche est vide, on affiche les scores
        vue.afficherScores(jeu);
    }
}
