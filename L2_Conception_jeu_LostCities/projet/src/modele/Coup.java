package modele;

// La classe Coup représente une action que va effectuer un joueur pendant son tour.
// Un coup est défini par deux informations :
// - l'indice de la carte concernée dans la main du joueur
// - le type de coup (jouer la carte ou la défausser)
public class Coup {

    // Indice de la carte dans la main du joueur
    // Cet indice correspond à la position de la carte dans la main
    private int indiceCarte;

    // Type de coup à effectuer (JOUER ou DEFAUSSER)
    // Il indique ce que le joueur souhaite faire avec la carte
    private TypeCoup typeCoup;

    // Constructeur du coup
    // Il permet de créer un coup en précisant
    // quelle carte est concernée et quel type d'action sera réalisée
    public Coup(int indiceCarte, TypeCoup typeCoup) {
        this.indiceCarte = indiceCarte;
        this.typeCoup = typeCoup;
    }

    // Méthode qui retourne l'indice de la carte
    public int getIndice() {
        return indiceCarte;
    }

    // Méthode qui retourne le type du coup
    // Elle permet de savoir si la carte doit être jouée ou défaussée
    public TypeCoup getType() {
        return typeCoup;
    }

    // Méthode toString pour afficher le coup sous forme lisible
    @Override
    public String toString() {
        return getType() + " " + getIndice() + " carte";
    }
}
