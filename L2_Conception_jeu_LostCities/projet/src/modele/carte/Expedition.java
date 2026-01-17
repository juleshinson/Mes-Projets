package modele.carte;


// La classe Expedition représente une expédition d'une couleur donnée pour un joueur.
// Elle hérite de PaquetCouleur, donc elle contient une liste de cartes d'une seule couleur.
// Une expédition correspond à une pile de cartes posées par le joueur pendant la partie.
public class Expedition extends PaquetCouleur {

    // Constructeur de l'expédition
    // On reçoit la couleur de l'expédition (JAUNE, BLEU, etc.)
    // Cette couleur est transmise à la classe mère PaquetCouleur
    public Expedition(Couleur couleur) {
        super(couleur);
    }

    // Méthode qui indique si une carte peut être posée sur cette expédition
    // Elle redirige simplement vers une méthode plus détaillée
    @Override
    public boolean peutPoser(Carte carte) {
        return peutposerCarte(carte);
    }

    // Méthode interne qui contient toute la logique pour savoir si une carte peut être posée sur l'expédition
    private boolean peutposerCarte(Carte carte) {

        // Si l'expédition est vide, on peut toujours poser une carte
        // Cela correspond au début d'une expédition
        if (estVide()) {
            return true;
        }

        // On récupère la dernière carte posée sur l'expédition
        Carte sommet = voirDernierCarte();

        // Si la dernière carte est une carte PARI,
        // alors on peut poser n'importe quelle carte par-dessus
        if (sommet.getType() == TypeCarte.PARI) {
            return true;
        }

        // Sinon, on vérifie que la valeur de la nouvelle carte est strictement supérieure à celle de la carte au sommet
        // Cela garantit que les cartes sont posées dans l'ordre croissant
        return carte.getType().getValeur() > sommet.getType().getValeur();
    }

    // Cette méthode calcule le score total de l'expédition
    // Le score dépend des cartes posées et du nombre de cartes PARI
    public int calculerScore() {

        // Si l'expédition est vide, le score est zéro
        if (estVide()) {
            return 0;
        }

        // On compte le nombre de cartes PARI présentes dans l'expédition
        // Les cartes PARI servent de multiplicateur pour le score
        long nbParis = cartes().filter(c -> c.getType() == TypeCarte.PARI).count();

        // On calcule la somme des valeurs de toutes les cartes de l'expédition
        int somme = cartes().mapToInt(c -> c.getType().getValeur()).sum();

        // Calcul du score de base :
        // - On retire 20 à la somme
        // - On multiplie par (nombre de PARI + 1)
        long score = (somme - 20) * (nbParis + 1);

        // Bonus : si l'expédition contient au moins 8 cartes,
        // on ajoute 20 points supplémentaires
        if (getNbCartes() >= 8) {
            return (int) (score + 20);
        }

        // Sinon, on retourne simplement le score calculé
        return (int) score;
    }

    // Méthode toString pour afficher le contenu de l'expédition
    // Elle retourne la liste des cartes sous forme de texte
    @Override
    public String toString() {
        return cartes.toString();
    }
}
