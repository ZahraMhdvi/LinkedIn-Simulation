package src.DataStructures.BPTree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;



public class BPlusTree<K extends Comparable<K>, T> {

    public Node<K,T> root;
    public static final int D = 2;

    public List<T> search(K key) {
        if (key == null || root == null) {
            return Collections.emptyList();
        }

        LeafNode<K, T> leaf = (LeafNode<K, T>) treeSearch(root, key);
        List<T> results = new ArrayList<>();
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (key.compareTo(leaf.keys.get(i)) == 0) {
                results.add(leaf.values.get(i));
            }
        }

        LeafNode<K, T> currentLeaf = leaf.nextLeaf;
        while (currentLeaf != null) {
            for (int i = 0; i < currentLeaf.keys.size(); i++) {
                if (key.compareTo(currentLeaf.keys.get(i)) == 0) {
                    results.add(currentLeaf.values.get(i));
                }
            }
            currentLeaf = currentLeaf.nextLeaf;
        }

        return results;
    }

    private Node<K, T> treeSearch(Node<K, T> node, K key) {
        if (node.isLeafNode) {
            return node;
        }

        else {
            IndexNode<K, T> index = (IndexNode<K, T>) node;

            if (key.compareTo(index.keys.get(0)) < 0) {
                return treeSearch((Node<K, T>) index.children.get(0), key);
            } else if (key.compareTo(index.keys.get(node.keys.size() - 1)) >= 0) {
                return treeSearch((Node<K, T>) index.children.get(index.children.size() - 1), key);
            }

            else {
                for (int i = 0; i < index.keys.size() - 1; i++) {
                    if (key.compareTo(index.keys.get(i)) >= 0 && key.compareTo(index.keys.get(i + 1)) < 0) {
                        return treeSearch((Node<K, T>) index.children.get(i + 1), key);
                    }
                }
            }
            return null;
        }
    }
    public void insert(K key, T value) {
        LeafNode<K,T> newLeaf = new LeafNode<K,T>(key, value);
        Entry<K, Node<K,T>> entry = new AbstractMap.SimpleEntry<K, Node<K,T>>(key, newLeaf);

        if(root == null || root.keys.size() == 0) {
            root = entry.getValue();
        }

        Entry<K, Node<K,T>> newChildEntry = getChildEntry(root, entry, null);

        if(newChildEntry == null) {
            return;
        } else {
            IndexNode<K,T> newRoot = new IndexNode<K,T>(newChildEntry.getKey(), root,
                    newChildEntry.getValue());
            root = newRoot;
            return;
        }
    }

    private Entry<K, Node<K,T>> getChildEntry(Node<K,T> node, Entry<K, Node<K,T>> entry,
                                              Entry<K, Node<K,T>> newChildEntry) {
        if(!node.isLeafNode) {
            IndexNode<K,T> index = (IndexNode<K,T>) node;
            int i = 0;
            while(i < index.keys.size()) {
                if(entry.getKey().compareTo(index.keys.get(i)) < 0) {
                    break;
                }
                i++;
            }
            newChildEntry = getChildEntry((Node<K,T>) index.children.get(i), entry, newChildEntry);

            if(newChildEntry == null) {
                return null;
            }
            else {
                int j = 0;
                while (j < index.keys.size()) {
                    if(newChildEntry.getKey().compareTo(index.keys.get(j)) < 0) {
                        break;
                    }
                    j++;
                }

                index.insertSorted(newChildEntry, j);

                if(!index.isOverflowed()) {
                    return null;
                }
                else{
                    newChildEntry = splitIndexNode(index);

                    if(index == root) {
                        IndexNode<K,T> newRoot = new IndexNode<K,T>(newChildEntry.getKey(), root,
                                newChildEntry.getValue());
                        root = newRoot;
                        return null;
                    }
                    return newChildEntry;
                }
            }
        }
        else {
            LeafNode<K,T> leaf = (LeafNode<K,T>)node;
            LeafNode<K,T> newLeaf = (LeafNode<K,T>)entry.getValue();

            leaf.insertSorted(entry.getKey(), newLeaf.values.get(0));

            if(!leaf.isOverflowed()) {
                return null;
            }
            else {
                newChildEntry = splitLeafNode(leaf);
                if(leaf == root) {
                    IndexNode<K,T> newRoot = new IndexNode<K,T>(newChildEntry.getKey(), leaf,
                            newChildEntry.getValue());
                    root = newRoot;
                    return null;
                }
                return newChildEntry;
            }
        }
    }

    public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {
        ArrayList<K> newKeys = new ArrayList<K>();
        ArrayList<T> newValues = new ArrayList<T>();

        for(int i=D; i<=2*D; i++) {
            newKeys.add(leaf.keys.get(i));
            newValues.add(leaf.values.get(i));
        }

        for(int i=D; i<=2*D; i++) {
            leaf.keys.remove(leaf.keys.size()-1);
            leaf.values.remove(leaf.values.size()-1);
        }

        K splitKey = newKeys.get(0);
        LeafNode<K,T> rightNode = new LeafNode<K,T>(newKeys, newValues);

        LeafNode<K,T> tmp = leaf.nextLeaf;
        leaf.nextLeaf = rightNode;
        leaf.nextLeaf.previousLeaf = rightNode;
        rightNode.previousLeaf = leaf;
        rightNode.nextLeaf = tmp;

        Entry<K, Node<K,T>> newChildEntry = new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rightNode);

        return newChildEntry;
    }


    public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {
        ArrayList<K> newKeys = new ArrayList<K>();
        ArrayList<Node<K,T>> newChildren = new ArrayList<Node<K,T>>();

        K splitKey = index.keys.get(D);
        index.keys.remove(D);

        newChildren.add(index.children.get(D+1));
        index.children.remove(D+1);

        while(index.keys.size() > D) {
            newKeys.add(index.keys.get(D));
            index.keys.remove(D);
            newChildren.add(index.children.get(D+1));
            index.children.remove(D+1);
        }

        IndexNode<K,T> rightNode = new IndexNode<K,T>(newKeys, newChildren);
        Entry<K, Node<K,T>> newChildEntry = new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rightNode);

        return newChildEntry;
    }


    public void delete(K key) {
        if(key == null || root == null) {
            return;
        }

        LeafNode<K,T> leaf = (LeafNode<K,T>)treeSearch(root, key);
        if(leaf == null) {
            return;
        }

        Entry<K, Node<K,T>> entry = new AbstractMap.SimpleEntry<K, Node<K,T>>(key, leaf);

        Entry<K, Node<K,T>> oldChildEntry = deleteChildEntry(root, root, entry, null);

        if(oldChildEntry == null) {
            if(root.keys.size() == 0) {
                if(!root.isLeafNode) {
                    root = ((IndexNode<K,T>) root).children.get(0);
                }
            }
            return;
        }
        else {
            int i = 0;
            K oldKey = oldChildEntry.getKey();
            while(i < root.keys.size()) {
                if(oldKey.compareTo(root.keys.get(i)) == 0) {
                    break;
                }
                i++;
            }
            if(i == root.keys.size()) {
                return;
            }
            root.keys.remove(i);
            ((IndexNode<K,T>)root).children.remove(i+1);
            return;
        }
    }

    private Entry<K, Node<K,T>> deleteChildEntry(Node<K,T> parentNode, Node<K,T> node,
                                                 Entry<K, Node<K,T>> entry, Entry<K, Node<K,T>> oldChildEntry) {
        if(!node.isLeafNode) {
            IndexNode<K,T> index = (IndexNode<K,T>)node;
            int i = 0;
            K entryKey = entry.getKey();
            while(i < index.keys.size()) {
                if(entryKey.compareTo(index.keys.get(i)) < 0) {
                    break;
                }
                i++;
            }
            oldChildEntry = deleteChildEntry(index, index.children.get(i), entry, oldChildEntry);

            if(oldChildEntry == null) {
                return null;
            }
            else {
                int j = 0;
                K oldKey = oldChildEntry.getKey();
                while(j < index.keys.size()) {
                    if(oldKey.compareTo(index.keys.get(j)) == 0) {
                        break;
                    }
                    j++;
                }
                index.keys.remove(j);
                index.children.remove(j+1);

                if(!index.isUnderflowed() || index.keys.size() == 0) {
                    return null;
                }
                else {
                    if(index == root) {
                        return oldChildEntry;
                    }
                    int s = 0;
                    K firstKey = index.keys.get(0);
                    while(s < parentNode.keys.size()) {
                        if(firstKey.compareTo(parentNode.keys.get(s)) < 0) {
                            break;
                        }
                        s++;
                    }

                    int splitKeyPos;
                    IndexNode<K,T> parent = (IndexNode<K,T>)parentNode;

                    if(s > 0 && parent.children.get(s-1) != null) {
                        splitKeyPos = handleIndexNodeUnderflow(
                                (IndexNode<K,T>)parent.children.get(s-1), index, parent);
                    } else {
                        splitKeyPos = handleIndexNodeUnderflow(
                                index, (IndexNode<K,T>)parent.children.get(s+1), parent);
                    }

                    if(splitKeyPos == -1) {
                        return null;
                    }

                    else {
                        K parentKey = parentNode.keys.get(splitKeyPos);
                        oldChildEntry = new AbstractMap.SimpleEntry<K, Node<K,T>>(parentKey, parentNode);
                        return oldChildEntry;
                    }
                }
            }
        }

        else {
            LeafNode<K,T> leaf = (LeafNode<K,T>)node;

            for(int i=0; i<leaf.keys.size(); i++) {
                if(leaf.keys.get(i) == entry.getKey()) {
                    leaf.keys.remove(i);
                    leaf.values.remove(i);
                    break;
                }
            }
            if(!leaf.isUnderflowed()) {
                return null;
            }
            else {

                if(leaf == root || leaf.keys.size() == 0) {
                    return oldChildEntry;
                }
                int splitKeyPos;
                K firstKey = leaf.keys.get(0);
                K parentKey = parentNode.keys.get(0);

                if(leaf.previousLeaf != null && firstKey.compareTo(parentKey) >= 0) {
                    splitKeyPos = handleLeafNodeUnderflow(leaf.previousLeaf, leaf, (IndexNode<K,T>)parentNode);
                } else {
                    splitKeyPos = handleLeafNodeUnderflow(leaf, leaf.nextLeaf, (IndexNode<K,T>)parentNode);
                }
                if(splitKeyPos == -1) {
                    return null;
                }
                else {
                    parentKey = parentNode.keys.get(splitKeyPos);
                    oldChildEntry = new AbstractMap.SimpleEntry<K, Node<K,T>>(parentKey, parentNode);
                    return oldChildEntry;
                }
            }
        }
    }


    public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
                                       IndexNode<K,T> parent) {

        int i = 0;
        K rKey = right.keys.get(0);
        while(i < parent.keys.size()) {
            if(rKey.compareTo(parent.keys.get(i)) < 0) {
                break;
            }
            i++;
        }

        if(left.keys.size() + right.keys.size() >= 2*D) {

            if(left.keys.size() > right.keys.size()) {
                while(left.keys.size() > D) {
                    right.keys.add(0, left.keys.get(left.keys.size()-1));
                    right.values.add(0, left.values.get(left.keys.size()-1));
                    left.keys.remove(left.keys.size()-1);
                    left.values.remove(left.values.size()-1);
                }
            }

            else {
                while(left.keys.size() < D) {
                    left.keys.add(right.keys.get(0));
                    left.values.add(right.values.get(0));
                    right.keys.remove(0);
                    right.values.remove(0);
                }
            }

            parent.keys.set(i-1, right.keys.get(0));

            return -1;
        }

        else {

            while(right.keys.size() > 0) {
                left.keys.add(right.keys.get(0));
                left.values.add(right.values.get(0));
                right.keys.remove(0);
                right.values.remove(0);
            }

            if(right.nextLeaf != null) {
                right.nextLeaf.previousLeaf = left;
            }
            left.nextLeaf = right.nextLeaf;

            return i-1;
        }
    }

    public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
                                        IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {

        int i = 0;
        K rKey = rightIndex.keys.get(0);
        while(i < parent.keys.size()) {
            if(rKey.compareTo(parent.keys.get(i)) < 0) {
                break;
            }
            i++;
        }

        if(leftIndex.keys.size() + rightIndex.keys.size() >= 2*D) {

            if(leftIndex.keys.size() > rightIndex.keys.size()) {
                while(leftIndex.keys.size() > D) {
                    rightIndex.keys.add(0, parent.keys.get(i-1));
                    rightIndex.children.add(leftIndex.children.get(leftIndex.children.size()-1));
                    parent.keys.set(i-1, leftIndex.keys.get(leftIndex.keys.size()-1));
                    leftIndex.keys.remove(leftIndex.keys.size()-1);
                    leftIndex.children.remove(leftIndex.children.size()-1);
                }
            }
            else {
                while(leftIndex.keys.size() < D) {
                    leftIndex.keys.add(parent.keys.get(i-1));
                    leftIndex.children.add(rightIndex.children.get(0));
                    parent.keys.set(i-1, rightIndex.keys.get(0));
                    rightIndex.keys.remove(0);
                    rightIndex.children.remove(0);
                }
            }
            return -1;
        }
        else {
            leftIndex.keys.add(parent.keys.get(i-1));
            while(rightIndex.keys.size() > 0) {
                leftIndex.keys.add(rightIndex.keys.get(0));
                leftIndex.children.add(rightIndex.children.get(0));
                rightIndex.keys.remove(0);
                rightIndex.children.remove(0);
            }
            leftIndex.children.add(rightIndex.children.get(0));
            rightIndex.children.remove(0);

            return i-1;
        }
    }
}