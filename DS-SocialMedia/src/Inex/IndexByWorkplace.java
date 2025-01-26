package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexByWorkplace {
    private BPlusTree bpt;

    public IndexByWorkplace(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        int key = person.getWorkplace().hashCode();
        this.bpt.insert(key, person);
    }

    public List<User> searchByWorkplace(String workplace) {
        int key = workplace.hashCode();
        return this.bpt.search(key);
    }
}
