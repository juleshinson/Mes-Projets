package Etat;
/*
Plateau.java
Classe décrivant un plateau de jeu de Breakthrough.
Auteur: Rémi PALLEN.
Date de la dernière version: 03/02/2026.
 */

public class Plateau {
    private int largeur;
    private Case[][] plateau;

    public int getLargeur() {return this.largeur;}
    public Case[][] getPlateau() {return this.plateau;}

    public Plateau(int largeur) {
		this.largeur=largeur;
		this.plateau=new Case[8][largeur];
		for (int i=0;i<8;i++) {
	    	for (int j=0;j<largeur;j++) {
				plateau[i][j]=new Case();
				if (i<2) {
		    		plateau[i][j].remplace(Joueur.joueurBlanc);
				} else if (i>5) {
		    		plateau[i][j].remplace(Joueur.joueurNoir);
				}
	    	}
		}
    }

    public Joueur getCase(int i, int j) {
		Case c=getPlateau()[i][j];
		if (c.getOccupe()) {
	    	return c.getPion();
		} else {
	    	return null;
		}
    }

    public void modifie(int x, int y, Joueur j) {/*Met le pion du joueur j dans la case x y. Si j=null, vide la case.*/
		if (j==null) {
	    	getPlateau()[x][y].libere();
		} else {
	    	getPlateau()[x][y].remplace(j);
		}
	}

    public void afficher() {/*Essayez d'améliorer cette méthode!*/
		String s=" ";
		for (int k=1;k<=this.largeur;k++) {
	    	s+=" "+k;
		}
		System.out.println(s);
		for (int i=0;i<8;i++) {
	    	System.out.print(i+1);
	    	for (int j=0;j<this.largeur;j++) {
				System.out.print("|"+getPlateau()[i][j].toString());
	    	}
	    	System.out.println("|");
		}
    }
}
