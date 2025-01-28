package src.DataStructures.BPTree;

import java.util.ArrayList;
import java.util.List;

public class LeafNode<K extends Comparable<K>, T> extends Node<K, T> {
    public ArrayList<T> values;
    public LeafNode<K, T> nextLeaf;
    public LeafNode<K, T> previousLeaf;

    public LeafNode(K firstKey, T firstValue) {
        isLeafNode = true;
        keys = new ArrayList<>();
        values = new ArrayList<>();
        keys.add(firstKey);
        values.add(firstValue);
    }

    public LeafNode(List<K> newKeys, List<T> newValues) {
        isLeafNode = true;
        keys = new ArrayList<>(newKeys);
        values = new ArrayList<>(newValues);
    }

    public void insertSorted(K key, T value) {
        int position = 0;
        while (position < keys.size() && key.compareTo(keys.get(position)) > 0) {
            position++;
        }

        if (position >= keys.size() || !keys.get(position).equals(key)) {
            keys.add(position, key);
            values.add(position, value);
        } else {
            keys.add(position + 1, key);
            values.add(position + 1, value);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LeafNode{keys=").append(keys)
                .append(", values=").append(values)
                .append("}");
        return sb.toString();
    }
}
