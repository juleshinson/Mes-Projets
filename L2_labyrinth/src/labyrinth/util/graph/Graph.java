package labyrinth.util.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph<T> {
    private final Set<T> nodes;
    private final Map<T, Set<T>> edges;

    public Graph() {
        nodes = new HashSet<>();
        edges = new HashMap<>();
    }

    public void addNode(T node){

        nodes.add(node);
        // on 'init' la liste de voisins sinon addEdge plante
        if (!edges.containsKey(node)) {
            edges.put(node, new HashSet<>());
        }
    }

    public void addEdge(T from, T to){
        addNode(from);
        addNode(to);
        edges.get(from).add(to);
    }

    public Set<T> getNodes(){
        return nodes;
    }

    public Set<T> getSuccessors(T node){
        // si le noeud a pas de voisins on renvoie un set vide
        if (edges.get(node) == null) {
            return new HashSet<>();
        }
        return edges.get(node);
    }
}
