package modele.strategie;


import Vue.VueJeu;
import modele.Coup;
import modele.Jeu;
import modele.carte.Couleur;

// La classe Humain représente une stratégie contrôlée par un joueur humain.
// Les décisions ne sont pas automatiques : elles sont demandées à l'utilisateur
// via une vue (console).
public class Humain implements Strategie {

    // Référence vers la vue du jeu
    // Elle permet d'afficher l'état du jeu et de demander des choix à l'utilisateur
    private VueJeu vue;

    // Constructeur de la stratégie humaine
    // On lui fournit la vue qui sera utilisée pour interagir avec le joueur
    public Humain(VueJeu vue) {
        this.vue = vue;
    }

    // Méthode appelée pour choisir un coup
    // Le jeu est d'abord affiché, puis on demande au joueur humain quoi faire
    @Override
    public Coup choisirCoup(Jeu jeu) {
        this.vue.afficher(jeu);
        return vue.demanderCoup();
    }

    // Méthode appelée pour choisir d'où piocher
    // Le choix est entièrement fait par le joueur humain via la vue
    @Override
    public Couleur choisirPioche(Jeu jeu) {
        return vue.demanderPioche();
    }
}

