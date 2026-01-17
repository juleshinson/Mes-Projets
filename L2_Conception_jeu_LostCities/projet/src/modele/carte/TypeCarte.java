package modele.carte;

// L'énumération TypeCarte représente le type (ou la valeur) d'une carte.
// Chaque type de carte possède une valeur numérique associée, utilisée notamment pour les comparaisons et le calcul des scores.
public enum TypeCarte {

    // Carte PARI, valeur spéciale égale à 0
    // Elle sert de multiplicateur dans le calcul du score
    PARI(0),

    // Cartes chiffrées allant de 2 à 10
    DEUX(2),
    TROIS(3),
    QUATRE(4),
    CINQ(5),
    SIX(6),
    SEPT(7),
    HUIT(8),
    NEUF(9),
    DIX(10);

    // Valeur numérique associée au type de carte
    private final int valeur;

    // Constructeur de l'énumération
    // Il permet d'associer une valeur numérique à chaque type de carte
    TypeCarte(int valeur) {
        this.valeur = valeur;
    }

    // Méthode qui retourne la valeur numérique du type de carte
    // Elle est utilisée dans les comparaisons et les calculs de score
    public int getValeur() {
        return valeur;
    }
}

