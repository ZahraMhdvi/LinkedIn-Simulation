package DataStructures.Graph;

import java.util.*;

public class AdjMapGraph<V, E> implements Graph<V, E> {

    private Map<V, Map<V, E>> adjacencyMap;

    public AdjMapGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    public Map<V, Map<V, E>> getAdjacencyMap() {
        return adjacencyMap;
    }

    @Override
    public int numVertices() {
        return this.adjacencyMap.size();
    }

    @Override
    public ArrayList<V> vertices() {
        return new ArrayList<>(getAdjacencyMap().keySet());
    }

    @Override
    public int numEdges() {
        int edgesCounter = 0;
        for (Map<V, E> edges : getAdjacencyMap().values()) {
            edgesCounter += edges.size();
        }
        return edgesCounter / 2;
    }

    @Override
    public Set<E> edges() {
        Set<E> edgesSet = new HashSet<>();
        for (Map<V, E> adjacent : getAdjacencyMap().values()) {
            edgesSet.addAll(adjacent.values());
        }
        return edgesSet;
    }

    @Override
    public E getEdge(V u, V v) {
        if (getAdjacencyMap().containsKey(u)) {
            Map<V, E> UAdjacent = getAdjacencyMap().get(u);
            return UAdjacent.get(v);
        }
        return null;
    }

    @Override
    public ArrayList<V> endVertices(E e) {
        return null;
    }

    @Override
    public V opposite(V v, E e) {
        return null;
    }

    @Override
    public int outDegree(V v) {
        return 0;
    }

    @Override
    public int inDegree(V v) {
        return 0;
    }

    @Override
    public ArrayList<E> outgoingEdges(V v) {
        return null;
    }

    @Override
    public ArrayList<E> incomingEdges(V v) {
        return null;
    }

    @Override
    public void insertVertex(V x) {

    }

    @Override
    public void insertEdge(V u, V v, E x) {

    }

    @Override
    public void removeVertex(V v) {

    }

    @Override
    public void removeEdge(E e) {

    }
}