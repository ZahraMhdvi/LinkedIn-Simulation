package DataStructures.Table;

import DataStructures.BPTree.BPlusTree;
import DataStructures.BPTree.IndexNode;

import DataStructures.BPTree.Node;
import DataStructures.BPTree.LeafNode;

import java.util.*;


public class Table<K extends Comparable<K>, T> {

    private String tableName;
    private List<String> columnNames;
    private BPlusTree<K, Node<K, T>> bPlusTree;

    public Table(String tableName, List<String> columnNames) {
        this.tableName = tableName;
        this.columnNames = new ArrayList<>(columnNames);
        this.bPlusTree = new BPlusTree<>();
    }

    public void insert(K key, T value) {
        LeafNode<K, T> newLeafNode = new LeafNode<>(key, value);
        bPlusTree.insert(key, newLeafNode);
    }



    public void delete(K key) {
        bPlusTree.delete(key);
    }

    public List<Node<K, T>> search(K key) {
        List<Node<K, T>> nodes = bPlusTree.search(key);

        if (nodes.isEmpty()) {
            System.out.println("No records found for key: " + key);
            return null;
        } else {
            return nodes;
        }
    }

    private void displayMatchingLeafNode(LeafNode<K, T> leaf, K key) {
        for (int i = 0; i < leaf.getKeys().size(); i++) {
            if (leaf.getKeys().get(i).equals(key)) {
                T value = leaf.values.get(i);
                System.out.println("Key: " + key + ", Data: " + value);
            }
        }
    }

    public void displayTable() {
        System.out.println("Table: " + tableName);
        System.out.println("Columns: " + String.join(", ", columnNames));
        System.out.println("----------------------------------------------------------------");

        LeafNode<K, T> current = getLeftmostLeafNode();

        while (current != null) {
            displayLeafNode(current);
            current = current.nextLeaf;
        }

        System.out.println("----------------------------------------------------------------");
    }


    private LeafNode<K, T> getLeftmostLeafNode() {
        Node<K, T> current = (Node<K, T>) bPlusTree.root;

        while (current != null && !current.isLeafNode()) {
            current = ((IndexNode<K, T>) current).children.get(0);
        }

        return (LeafNode<K, T>) current;
    }
    private void displayLeafNode(LeafNode<K, T> leaf) {
        for (int i = 0; i < leaf.getKeys().size(); i++) {
            K key = leaf.getKeys().get(i);
            T value = leaf.values.get(i);
            System.out.println("Key: " + key + ", Data: " + value);
        }
    }













}
