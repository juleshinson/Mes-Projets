package Etat;

/*
Case.java
Classe décrivant l'état d'une case du plateau de jeu de Breakthrough.
Auteur: Rémi PALLEN.
Date de la dernière version: 03/02/2026.
 */

public class Case {
    private boolean occupe; /*=true si la case est occupée, false sinon.*/
    private Joueur pion; /*Donne le joueur occupant la case si celle-ci est occupée.*/

    public Case() {
	    this.occupe=false;
	    this.pion=new Joueur();
    }

    public boolean getOccupe() {return this.occupe;}
    public Joueur getPion() {return this.pion;}

    private void setOccupe(boolean b) {
	this.occupe=b;
    }

    private void setPion(Joueur j) {
	this.pion=j;
    }

    public void libere() {setOccupe(false);}
    public void prendre() {setOccupe(true);}

    public void remplace(Joueur j) {/*Met le joueur j dans la case*/
	    prendre();
	    setPion(j);
    }

    public String toString() {
	    if (getOccupe()) {
	        return this.pion.toString();
	    } else {
	        return "_";
	    }
    }
}
