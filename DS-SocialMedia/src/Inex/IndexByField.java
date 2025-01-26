package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexByField {
    private BPlusTree bpt;

    public IndexByField(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        int key = person.getField().hashCode();
        this.bpt.insert(key, person);
    }

    public List<User> searchByField(String field) {
        int key = field.hashCode();
        return this.bpt.search(key);
    }
}
