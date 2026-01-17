package modele.carte;

// La classe Defausse représente une pile de défausse pour une couleur donnée.
// Elle hérite de PaquetCouleur, donc elle contient déjà une liste de cartes associées à une couleur précise.
// Elle implémente Piochable, ce qui signifie qu'on peut piocher des cartes dedans.
public class Defausse extends PaquetCouleur implements Piochable {

    // Constructeur de la défausse
    // On lui passe une couleur, et on la transmet au constructeur de la classe mère
    // Cela permet de créer une défausse spécifique à une couleur (JAUNE, BLEU, etc.)
    public Defausse(Couleur couleur) {
        super(couleur);
    }

    // Cette méthode indique si une carte peut être posée dans cette défausse
    // Une carte peut être défaussée ici uniquement si elle a la même couleur
    // que la défausse elle-même
    @Override
    public boolean peutPoser(Carte carte) {
        return carte.getCouleur() == getCouleur();
    }

    // Cette méthode permet de prendre une carte depuis la défausse
    // Elle est appelée quand un joueur décide de piocher dans une défausse
    @Override
    public Carte prendreCarte() {

        // Si la défausse est vide, il n'y a aucune carte à prendre
        // On retourne donc null
        if (estVide()) {
            return null;
        }

        // Sinon, on prend la dernière carte ajoutée dans la défausse
        // Cela correspond au fonctionnement normal d'une pile
        return prendreDerniereCarte();
    }

    // Méthode toString pour afficher le contenu de la défausse
    // Elle retourne simplement la liste des cartes sous forme de texte
    public String toString() {
        return cartes.toString();
    }
}

