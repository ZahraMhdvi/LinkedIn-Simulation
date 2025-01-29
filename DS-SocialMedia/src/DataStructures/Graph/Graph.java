package src.DataStructures.Graph;

import java.util.*;

public interface Graph<V, E> {

    int numVertices();

    ArrayList<V> vertices();

    int numEdges();

    Set<E> edges();

    E getEdge(V u, V v);

    ArrayList<V> endVertices(E e);

    V opposite(V v, E e);

    int outDegree(V v);

    int inDegree(V v);

    ArrayList<E> outgoingEdges(V v);

    ArrayList<E> incomingEdges(V v);

    void insertVertex(V x);

    void insertEdge(V u, V v, E x);

    void removeVertex(V v);

    void removeEdge(E e);
}