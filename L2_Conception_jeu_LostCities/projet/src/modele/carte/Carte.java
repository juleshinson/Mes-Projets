package modele.carte;

import java.util.Comparator;

// La classe Carte représente une carte du jeu.
// Une carte est définie par deux choses : une couleur et un type.
// Elle implémente Comparable pour pouvoir comparer des cartes entre elles
public class Carte implements Comparable<Carte> {

    // Couleur de la carte (JAUNE, BLANC, BLEU, VERT, ROUGE)
    // Elle est finale car une carte ne change jamais de couleur
    private final Couleur couleur;

    // Type de la carte (PARI, DEUX, TROIS, ..., DIX)
    // Elle est aussi finale car le type d'une carte est fixe
    private final TypeCarte type;

    // Constructeur de la carte
    // Il permet de créer une carte en précisant sa couleur et son type
    public Carte(Couleur couleur, TypeCarte type) {
        this.couleur = couleur;
        this.type = type;
    }

    // Méthode qui retourne la couleur de la carte
    // Elle permet aux autres classes de connaître la couleur
    public Couleur getCouleur() {
        return couleur;
    }

    // Méthode qui retourne le type de la carte
    // Elle permet par exemple de connaître la valeur de la carte
    public TypeCarte getType() {
        return type;
    }

    // Comparateur statique pour comparer deux cartes
    // D'abord on compare les cartes par couleur
    // Si les couleurs sont identiques, on compare ensuite par type
    // Ce comparateur est utilisé dans compareTo
    public static final Comparator<Carte> CMP = Comparator.comparing(Carte::getCouleur).thenComparing(Carte::getType);

    // Méthode imposée par l'interface Comparable
    // Elle permet de dire comment comparer cette carte avec une autre carte
    // Ici, on utilise le comparateur défini juste au-dessus
    @Override
    public int compareTo(Carte o) {
        return CMP.compare(this, o);
    }

    // Méthode qui permet d'afficher la carte sous forme de texte
    // Elle est très utile pour le débogage et l'affichage dans la console
    // Exemple : JAUNE-DEUX ou ROUGE-PARI
    @Override
    public String toString() {
        return getCouleur() + "-" + getType();
    }
}

