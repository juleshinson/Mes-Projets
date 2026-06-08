package Etat;
/*
Joueur.java
Classe décrivant un joueur du jeu de Breakthrough.
Auteurs: Rémi PALLEN, Jean LIEBER.
Date de la dernière version: 03/02/2026.
 */

public class Joueur {
    public static Joueur joueurBlanc=new Joueur();
    public static Joueur joueurNoir=new Joueur();

    public Joueur autreJoueur () {
		if (this==joueurBlanc) {
	    	return joueurNoir;
		};
		if (this==joueurNoir) {
	    	return joueurBlanc;
		};
		return null;
    	}

    public String toString() {
		if (this==joueurBlanc) {
	    	return "B";
		};
		if (this==joueurNoir) {
	    	return "N";
		};
		return "?";
    }
}
