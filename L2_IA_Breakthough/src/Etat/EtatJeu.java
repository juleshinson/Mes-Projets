

package Etat;/*
EtatJeu.java
Classe décrivant une configuration du jeu (le joueur dont c'est le tour ainsi que l'état du plateau de jeu).
Auteur: Rémi PALLEN.
Date de la dernière version: 03/02/2026.
 */

import java.util.ArrayList;
import java.util.List;

public class EtatJeu {
    static private int largeur; /*Désigne le nombre de colonne du plateau de jeu*/
    private Plateau  plateau;
    private boolean tourBlanc; /*=true si c'est le tour du Joueur blanc, false sinon.*/
	private boolean echec; //état invalide

    public int getLargeur() {return this.largeur;};
    public boolean getTourBlanc() {return this.tourBlanc;};
    public Plateau getPlateau() {return this.plateau;};
	public boolean getEchec() {return this.echec;};

    public EtatJeu (int largeur) {
		this.largeur=largeur;
        this.plateau=new Plateau(largeur);
		this.tourBlanc=true; /*Le joueur Blanc commence.*/
		this.echec = false; //de base ce n'est pas un echec
    }

	public static EtatJeu etatEchec(int largeur){
		EtatJeu e = new EtatJeu(largeur); //on crée un nouveau jeu normal
		e.echec=true; //mais on initialise son echec a vrai
		return e;
	}

    public void modifie(int x,int y, Joueur j) { /*Remplie la case de la ligne x et de la colonne y par le pion du joueur j. Si j=null, alors vide la case.*/
	getPlateau().modifie(x,y,j);
    }

    public Gagnant etatFinal() { /*Renvoi AUCUN si l'état n'est pas final, le gagnant sinon.*/
		for (int k=0;k<getLargeur();k++) {
	    	if (getPlateau().getCase(0,k)==Joueur.joueurNoir) {
				return Gagnant.NOIR;
	    	}
		}
		for (int k=0;k<largeur;k++) {
	    	if (getPlateau().getCase(7,k)==Joueur.joueurBlanc) {
				return Gagnant.BLANC;
	    	}
		}
		return Gagnant.AUCUN;
    }

    /*---------------------------------------------Ne programmez qu'en dessous de cette ligne-là! (Sauf pour l'importation de modules)------------------------------------------------------------------*/

	public EtatJeu copie(int largeur){ //fais la copie d'un etat
		EtatJeu copie = new EtatJeu(getLargeur());
		copie.tourBlanc = this.tourBlanc; // copie le joueur courant (bug corrigé en faisant Monte-Carlo)
		for(int i = 0; i < 8; i++){
			for(int k = 0; k < largeur; k++){
				Joueur contenu = this.getPlateau().getCase(i, k);
				copie.modifie(i, k, contenu);
			}
		}
		return copie;
	}

	public EtatJeu successeur(Action a, Joueur j) {

		if(this.actionValide(a, j)){ //si le coup est valide
			int x = a.getLigne(); //on récupère les coordonnées à déplacer
			int y = a.getColonne();
			int d = a.getDirection();

			int dx; //sens du déplacement
			if(j == Joueur.joueurBlanc) {
				dx = 1;  //le blanc avance vers le bas
			}else {
				dx = -1; //le noir avant vers le haut
			}
			//coodornnées d'arrivé
			int x_arriver = x + dx;
			int y_arriver = y + d;

			//on fait une recopie de l'état actuel pour ne pas modifier l'état courant
			EtatJeu nouvelEtat = copie(getLargeur());

			//on enlève le pion de la case de départ en mettant j à null
			nouvelEtat.modifie(x, y, null);

			//on met le pion sur la case d'arrivé
			nouvelEtat.modifie(x_arriver, y_arriver, j);

			//on passe au joueur suivant
			if(this.getTourBlanc()){ //si c'était au tour du joueur blanc alors on met à false
				nouvelEtat.tourBlanc = false;
			}else{ //sinon c'est donc son tour et on met a true
				nouvelEtat.tourBlanc = true;
			}
			return nouvelEtat;
		}
		return etatEchec(largeur); //on retourne l'état echec
	}

	public Boolean actionValide(Action a, Joueur j) { //verifier si une action d'un joueur est valide
		int ligne_de_depart = a.getLigne();
		int colonne_de_depart = a.getColonne();
		int direction = a.getDirection();

		if(direction != -1 && direction != 0 && direction != 1){ //verification direction valide
			return false;
		}
		if(ligne_de_depart < 0 || ligne_de_depart >= 8){ //verification si on est dans le plateau
			return false;
		}
		if(colonne_de_depart < 0 || colonne_de_depart >= getLargeur()){ //verification si on est dans le plateau
			return false;
		}
		Joueur contenuDepart = this.getPlateau().getCase(ligne_de_depart, colonne_de_depart); //la case doit contenir un pion du joueur
		if(!contenuDepart.equals(j)) {
			return false;
		}
		int direction_arrive;
		if(j.equals(Joueur.joueurBlanc)) { //calcule de la case d'arrivé
			direction_arrive = 1;
		}else{
			direction_arrive = -1;
		}
		//calcule coordonnées arrivé
		int ligne_arriver = ligne_de_depart + direction_arrive;
		int colonne_arriver = colonne_de_depart + direction;
		if(ligne_arriver < 0 || ligne_arriver >= 8){ //verification si on est dans le plateau
			return false;
		}
		if(colonne_arriver < 0 || colonne_arriver >= getLargeur()){ //verification si on est dans le plateau
			return false;
		}
		Joueur contenuArriver = this.getPlateau().getCase(ligne_arriver, colonne_arriver); //on regarde le contenu du pion d'arriver
		if(direction == 0){ //si on avance tout droit
			if(contenuArriver == null){ //il faut que la case d'arriver soit vide
				return true;
			}else{
				return false;
			}
		}
		//si on est en diagonale alors
		if(contenuArriver == j.autreJoueur()){ //la case d'arriver contenant un pion adverse
			return true;
		}else {
			return false;
		}
	}

	public List<EtatJeu> successeurs(Joueur j) {
		List<EtatJeu> successeurs = new ArrayList<>(); //on crée une liste pour tous les successeurs
		for(int i = 0; i < 8; i++){
			for(int k = 0; k < getLargeur(); k++){ //pour chaque case
				Joueur contenu = this.getPlateau().getCase(i, k);
				if(contenu != null && contenu.equals(j)){ //pour un joueur, on fait toutes les actions (-1, 0, 1)
					for(int d = -1; d < 2; d++){ //pour boucler sur toutes les directions
						Action a = new Action(i, k, d);
						if(actionValide(a, j)){ //si l'action est valide on l'ajoute a la liste
							EtatJeu e = this.successeur(a, j);
							successeurs.add(e);
						}
					}
				}
			}
		}
		return successeurs;
	}

}
