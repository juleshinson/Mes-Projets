package Etat;
public class Action {
    private int ligne;
    private int colonne;
    private int direction; // -1 gauche; 0 devant et 1 à droite

    public Action(int ligne, int colonne, int direction) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.direction = direction;
    }
    //getters
    public int getLigne() {
        return  ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "(" + ligne + ";" + colonne + ";" + direction + ")";
    }
}
