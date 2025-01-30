package DataStructures.Graph;

import java.util.*;

public class AdjMapGraph<V, E> implements Graph<V, E> {

    private final Map<V, Map<V, E>> adjacencyMap;

    public AdjMapGraph() {
        this.adjacencyMap = new HashMap<>();
    }

    public Map<V, Map<V, E>> getAdjacencyMap() {
        return adjacencyMap;
    }

    @Override
    public int numVertices() {
        return getAdjacencyMap().size();
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
        ArrayList<V> verticesList = new ArrayList<>();
        for (Map<V, E> adjacent : getAdjacencyMap().values()) {
            for (Map.Entry<V, E> entry : adjacent.entrySet()) {
                if (entry.getValue().equals(e)) {
                    verticesList.add(entry.getKey());
                    verticesList.add(HelperFindKeyByValue(adjacent));
                    return verticesList;
                }
            }
        }
        return null;
    }

    private V HelperFindKeyByValue(Map<V, E> givenValue) {
        for (Map.Entry<V, Map<V, E>> entry : getAdjacencyMap().entrySet()) {
            if (entry.getValue().equals(givenValue))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public V opposite(V v, E e) {
        ArrayList<V> endVerticesOfE = endVertices(e);
        if (endVerticesOfE != null) {
            if (endVerticesOfE.get(0).equals(v))
                return endVerticesOfE.get(1);
            else if (endVerticesOfE.get(1).equals(v))
                return endVerticesOfE.get(0);
            else throw new IllegalArgumentException("v is not incident to this edge");
        }
        return null;
    }

    @Override
    public int outDegree(V v) {
        if (getAdjacencyMap().containsKey(v)) {
            return getAdjacencyMap().get(v).size();
        }
        return 0;
    }

    @Override
    public int inDegree(V v) {
        int inDegree = 0;
        for (Map<V, E> adjacent : getAdjacencyMap().values()) {
            for (V vertex : adjacent.keySet()) {
                if (vertex.equals(v)) {
                    inDegree++;
                }
            }
        }
        return inDegree;
    }

    @Override
    public ArrayList<E> outgoingEdges(V v) {
        if (getAdjacencyMap().containsKey(v)) {
            return new ArrayList<>(getAdjacencyMap().get(v).values());
        }
        return null;
    }

    @Override
    public ArrayList<E> incomingEdges(V v) {
        ArrayList<E> incomingEdges = new ArrayList<>();
        for (Map<V, E> adjacent : getAdjacencyMap().values()) {
            for (Map.Entry<V, E> entry : adjacent.entrySet()) {
                if (entry.getKey().equals(v)) {
                    incomingEdges.add(entry.getValue());
                }
            }
        }
        return incomingEdges;
    }

    @Override
    public void insertVertex(V x) {
        if (!getAdjacencyMap().containsKey(x)) {
            getAdjacencyMap().put(x, new HashMap<>());
        }
    }

    @Override
    public void insertEdge(V u, V v, E x) {
        if (getAdjacencyMap().containsKey(u) && getAdjacencyMap().containsKey(v)) {
            getAdjacencyMap().get(u).put(v, x);
            getAdjacencyMap().get(v).put(u, x);
        }
    }

    @Override
    public void removeVertex(V v) {
        if (getAdjacencyMap().containsKey(v)) {
            getAdjacencyMap().remove(v);
            for (Map<V, E> edges : getAdjacencyMap().values()) {
                edges.remove(v);
            }
        }
    }

    @Override
    public void removeEdge(E e) {
        for (Map<V, E> edges : getAdjacencyMap().values()) {
            edges.values().remove(e);
        }
    }

    public Set<V> getNeighbors(V vertex) {
        Set<V> neighbors = new HashSet<>();
        if (getAdjacencyMap().containsKey(vertex)) {
            Map<V, E> neighborsMap = getAdjacencyMap().get(vertex);
            neighbors.addAll(neighborsMap.keySet());
        }
        return neighbors;
    }
}