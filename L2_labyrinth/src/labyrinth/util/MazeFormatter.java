package labyrinth.util;

import labyrinth.model.*;

public class MazeFormatter {

    public String format(Tile tile) {


        boolean up = tile.getExits().contains(Direction.UP);
        boolean down = tile.getExits().contains(Direction.DOWN);
        boolean left = tile.getExits().contains(Direction.LEFT);
        boolean right = tile.getExits().contains(Direction.RIGHT);

        boolean presence_joueur0 = tile.hasPlayer(0);
        boolean presence_joueur1 = tile.hasPlayer(1);
        boolean presence_joueur2 = tile.hasPlayer(2);
        boolean presence_joueur3 = tile.hasPlayer(3);

        String centre = contenu(tile.getContent());

        //les caracteres unicode
        char joueur0 = '0';
        char joueur1 = '1';
        char joueur2 = '2';
        char joueur3 = '3';
        char topleft = '\u250C';
        char topright = '\u2510';
        char bottomleft = '\u2514';
        char bottomright = '\u2518';
        char ligne = '\u2500';
        StringBuilder horizontal = new StringBuilder();
        StringBuilder horizontalouvert = new StringBuilder();
        for(int i = 0; i < 10; i++){
            horizontal.append(ligne);
        }
        for(int i = 0; i < 10; i++){
            if(i == 3 || i == 4 || i == 5 || i == 6){
                horizontalouvert.append(" ");
            }else{
                horizontalouvert.append(ligne);
            }
        }
        char vertical = '\u2502';

        StringBuilder sb = new StringBuilder();

        // Ligne 1
        sb.append(topleft);
        sb.append(up ? horizontalouvert : horizontal);
        sb.append(topright);
        sb.append('\n');

        // Ligne 2
        sb.append(vertical);
        if (presence_joueur0 && presence_joueur1) {
            sb.append("  ").append(joueur0).append("    ").append(joueur1).append("  ");
        } else if (presence_joueur0) {
            sb.append("  ").append(joueur0).append("       ");
        } else if (presence_joueur1) {
            sb.append("       ").append(joueur1).append("  ");
        } else {
            sb.append("          ");
        }
        sb.append(vertical);
        sb.append('\n');

        // Ligne 3
        sb.append(left ? " " : String.valueOf(vertical));
        sb.append("    ").append(centre).append("     ");
        sb.append(right ? " " : String.valueOf(vertical));
        sb.append('\n');

        // Ligne 4
        sb.append(vertical);
        if (presence_joueur2 && presence_joueur3) {
            sb.append("  ").append(joueur2).append("    ").append(joueur3).append("  ");
        } else if (presence_joueur2) {
            sb.append("  ").append(joueur2).append("       ");
        } else if (presence_joueur3) {
            sb.append("       ").append(joueur3).append("  ");
        } else {
            sb.append("          ");
        }
        sb.append(vertical);
        sb.append('\n');

        // Ligne 5
        sb.append(bottomleft);
        sb.append(down ? horizontalouvert : horizontal);
        sb.append(bottomright);

        return sb.toString();
    }

    private String contenu(Content content){
        return switch (content) {
            case None n -> " ";
            case Treasure t -> "T" ;
            case StartingPosition s -> "J" ;
        };
    }

    public String format(Maze maze){
        int h = maze.getTiles().getHeight();
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < h; i++){
            if(i > 0){s.append("\n");}
            s.append(formatRow(maze, i));
        }
        return s.toString();
    }

    private String formatRow(Maze maze, int y) {
        int w =  maze.getTiles().getWidth();
        String[][] parts = new String[w][];

        for(int i = 0; i < w; i++){
            Tile tile = maze.getTiles().get(i, y);
            parts[i] = format(tile).split("\\n");
        }

        int l = parts[0].length;

        StringBuilder s = new StringBuilder();
        for(int line = 0; line < l; line++){
            if(line >0){s.append("\n");}
            for(int i = 0; i < w; i++){
                if(i > 0){s.append(" ");}
                s.append(parts[i][line]);
            }
        }
        return s.toString();
    }
}
