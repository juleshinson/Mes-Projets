package Etat;
/*
Gagnant.java
Classe décrivant les différents cas possibles des états du jeu:
-AUCUN: l'état n'est pas final,
-BLANC: l'état est final et le joueur Blanc a gagné,
-NOIR: l'état est final et le joueur Noir a gagné.

Auteur: Rémi PALLEN.
Date de la dernière version: 03/02/2026.
 */

public enum Gagnant {
    AUCUN,
    BLANC,
    NOIR
}
