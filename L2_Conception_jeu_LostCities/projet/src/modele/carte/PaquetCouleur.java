package modele.carte;


// La classe abstraite PaquetCouleur représente un paquet de cartes
public abstract class PaquetCouleur extends PaquetBase {

    // Couleur associée à ce paquet
    // Elle est finale car une fois créée, la couleur du paquet ne change jamais
    private final Couleur couleur;

    // Constructeur du paquet de couleur
    // Il initialise le paquet comme un paquet vide
    // et fixe définitivement la couleur du paquet
    public PaquetCouleur(Couleur couleur) {
        super();          // appel du constructeur de PaquetBase
        this.couleur = couleur;
    }

    // Méthode qui retourne la couleur du paquet
    // Elle permet de savoir à quelle couleur ce paquet correspond
    public Couleur getCouleur() {
        return couleur;
    }

    // Méthode abstraite qui indique si une carte peut être posée sur ce paquet
    // La règle dépend du type de paquet (expédition, défausse, etc.)
    public abstract boolean peutPoser(Carte carte);

    // Méthode qui permet de poser une carte sur le paquet
    // Elle vérifie d'abord si la carte peut être posée selon les règles
    // Si c'est le cas, la carte est ajoutée au paquet et la méthode retourne true
    // Sinon, la carte n'est pas ajoutée et la méthode retourne false
    public boolean poser(Carte carte) {
        if (peutPoser(carte)) {
            ajouterCartes(carte);
            return true;
        }
        return false;
    }
}

