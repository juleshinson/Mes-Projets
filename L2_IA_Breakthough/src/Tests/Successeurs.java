package Tests;

import Etat.Action;
import Etat.EtatJeu;
import Etat.Joueur;

import java.util.List;

public class Successeurs {
    public static void main(String[] args) {
        EtatJeu e = new EtatJeu(8);
        List<EtatJeu> liste = e.successeurs(Joueur.joueurBlanc);
        System.out.println(liste.size());
    }
}

