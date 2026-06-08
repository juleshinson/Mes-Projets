package labyrinth.util.graph;

import labyrinth.model.Direction;
import labyrinth.model.Maze;
import labyrinth.model.Tile;

public class MazeToGraphAdapter {
    public static Graph<Tile> getGraph(Maze maze){
        Graph<Tile> graph = new Graph<>(); //creation du graphe vide
        for(int x = 0; x < maze.getTiles().getWidth(); x++){ //parcours de toutes les lignes
            for(int y = 0; y < maze.getTiles().getHeight(); y++){ //parcours de toutes les colonnes
                Tile tile = maze.getTiles().get(x, y); //on prends la tuile a la position x, y
                graph.addNode(tile); //on ajoute au graphe
                //pour chaque sortie de la tuile
                for (Direction d : tile.getExits()){
                    //coordonnées du voisin
                    int nx = x;
                    int ny = y;
                    switch(d){//si la sortie est :
                        case UP://vers le haut
                            ny = y - 1;
                            break;
                        case DOWN://vers le bas
                            ny = y + 1;
                            break;
                        case LEFT://vers la gauche
                            nx = x - 1;
                            break;
                        case RIGHT://vers la droite
                            nx = x + 1;
                            break;
                    }
                    // on vérifie que le voisin est bien dans la grille avant d'appeler get()
                    /*if(maze.getTiles().get(nx, ny) != null){//si le voisin est dans la grille
                        Tile voisin = maze.getTiles().get(nx, ny); // on le met comme voisin
                        if(voisin.getExits().contains(d.getOpposite())){
                            graph.addEdge(voisin, tile); //ajout d'une arete
                        }
                    }*/
                    if (nx >= 0 && nx < maze.getTiles().getWidth() && ny >= 0 && ny < maze.getTiles().getHeight()) {
                        Tile voisin = maze.getTiles().get(nx, ny);
                        if(voisin != null && voisin.getExits().contains(d.getOpposite())){
                            graph.addEdge(tile, voisin);
                        }
                    }

                }
            }
        }
        return graph;
    }

}
