package Tests;

import Etat.EtatJeu;

public class AfficherPlateau {
    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        e.getPlateau().afficher();
    }
}
