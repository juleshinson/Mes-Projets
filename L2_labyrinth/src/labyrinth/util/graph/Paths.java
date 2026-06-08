package labyrinth.util.graph;

import java.util.*;

public class Paths<T> {
    private final Map<T,T> predecessors ;
    private final T from ;

    public Paths(Graph<T> graph, T from){
        this.from = from ;
        this.predecessors = new HashMap<>();
        Queue<T> file = new LinkedList<>();
        Set<T> visited = new HashSet<>();
        predecessors.put(from,null);

        file.add(from );
        visited.add(from);
        while(!file.isEmpty()){
            T current = file.poll(); //enlève et renvoie le premier
            for(T next : graph.getSuccessors(current)){
                if(!visited.contains(next)){
                    this.predecessors.put(next,current);
                    visited.add(next);
                    file.add(next);
                }
            }
        }
    }

    public boolean isAccessible(T node){
        // si le noeud est pas du tout dans predecessors il est pas accessible
        if (!predecessors.containsKey(node)) {
            return false;
        }
        T current =  node;
        while(current != null ){
            if (current.equals(from)){
                return true ;
            }
            current = predecessors.get(current);
        }
        return false ;
    }

    public List<T> getPath(T node){
        List<T> path = new ArrayList<>();
        T current =  node;
        while(current != null ){
            path.add(current);
            current = predecessors.get(current);

        }
        return path ;
    }
}
