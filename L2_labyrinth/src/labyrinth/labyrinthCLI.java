package labyrinth;

import labyrinth.model.*;
import labyrinth.util.MazeFormatter;

import java.util.Scanner;

public class labyrinthCLI {

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        int nbJoueurs = -1;
        while (nbJoueurs <= 0) {
            System.out.println("Veuillez entrer le nombre de joueurs (entier positif) :");
            if (sc.hasNextInt()) {
                nbJoueurs = sc.nextInt();
                if (nbJoueurs <= 0 || nbJoueurs > 4 ) {
                    System.out.println("Erreur : le nombre doit être strictement positif et inférieur à 4.");
                }
            } else {
                System.out.println("Erreur : vous devez entrer un entier.");
                sc.next(); // vider l'entrée invalide
            }
        }
        Game game = new Game(nbJoueurs) ;
        game.init();
        while(!game.getState().equals(GameState.GAME_OVER)){
            //on affiche le labyrinthe

            MazeFormatter formatter = new MazeFormatter();
            System.out.println(formatter.format(game.getMaze()));
            System.out.println(formatter.format(game.getMaze().getExtraTile()));
            System.out.println("Le joueur courant est "+game.getCurrentPlayer().getIndex());
            System.out.println("Son prochain trésor se trouve à "+ game.getCurrentPlayer().getDeck().getDrawPile().getFirst());
            System.out.println("Il lui reste à trouver "+ game.getCurrentPlayer().getDeck().getDrawPile().size() +"trésors");
            int rotation = -1;
            while (rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) {
                System.out.println("De combien voulez-vous tourner la tuile ? (0, 90, 180, 270)");
                if (sc.hasNextInt()) {
                    rotation = sc.nextInt();
                    if (rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) {
                        System.out.println("Erreur : rotation invalide.");
                    }
                } else {
                    System.out.println("Erreur : vous devez entrer un entier.");
                    sc.next();
                }
            }

            try {
                game.rotateExtraTile(rotation);
            } catch (Exception e) {
                System.out.println("Action impossible : " + e.getMessage());
                continue; // recommence le tour
            }
            int num = -1;
            while (num < 0) {
                System.out.println("Quelle ligne ou colonne voulez-vous déplacer ? (entier positif)");
                if (sc.hasNextInt()) {
                    num = sc.nextInt();
                    if (num < 0 || num > game.getMaze().getTiles().getWidth() || num > game.getMaze().getTiles().getHeight()) {
                        System.out.println("Erreur : valeur invalide.");
                    }
                } else {
                    System.out.println("Erreur : vous devez entrer un entier.");
                    sc.next();
                }
            }
            Direction direction = null;
            while (direction == null) {
                System.out.println("Dans quelle direction voulez-vous pousser ? (UP, DOWN, RIGHT, LEFT");
                String d = sc.next().trim().toUpperCase();
                switch (d) {
                    case "UP": direction = Direction.UP; break;
                    case "DOWN": direction = Direction.DOWN; break;
                    case "LEFT": direction = Direction.LEFT; break;
                    case "RIGHT": direction = Direction.RIGHT; break;
                    default:
                        System.out.println("Erreur : direction invalide.");
                }
            }

            try {
                game.moveMaze(new MazeMove(direction, num));
            } catch (Exception e) {
                System.out.println("Action impossible : " + e.getMessage());
                continue;
            }

            int x = -1, y = -1;

            while (x < 0) {
                System.out.println("Entrez la coordonnée x de la tuile :");
                if (sc.hasNextInt()) {
                    x = sc.nextInt();
                    if (x < 0) System.out.println("Erreur : x doit être positif.");
                } else {
                    System.out.println("Erreur : vous devez entrer un entier.");
                    sc.next();
                }
            }

            while (y < 0) {
                System.out.println("Entrez la coordonnée y de la tuile :");
                if (sc.hasNextInt()) {
                    y = sc.nextInt();
                    if (y < 0) System.out.println("Erreur : y doit être positif.");
                } else {
                    System.out.println("Erreur : vous devez entrer un entier.");
                    sc.next();
                }
            }


            try {
                game.movePlayer(game.getMaze().getTiles().get(x, y));
            } catch (Exception e) {
                System.out.println("Déplacement impossible : " + e.getMessage());
                continue;
            }

            try {
                game.endTurn();
            } catch (Exception e) {
                System.out.println("Erreur lors de la fin du tour : " + e.getMessage());
            }
        }
        System.out.println("Partie terminée !");


        }


    }



