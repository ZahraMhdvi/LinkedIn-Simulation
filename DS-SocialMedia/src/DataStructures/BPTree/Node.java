package src.DataStructures.BPTree;

import java.util.ArrayList;

public class Node<K extends Comparable<K>, T> {
    protected boolean isLeafNode;
    protected ArrayList<K> keys;

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public void setLeafNode(boolean leafNode) {
        isLeafNode = leafNode;
    }

    public ArrayList<K> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<K> keys) {
        this.keys = keys;
    }

    public boolean isOverflowed() {
        return keys.size() > 2 * BPlusTree.D;
    }

    public boolean isUnderflowed() {
        return keys.size() < BPlusTree.D;
    }

}