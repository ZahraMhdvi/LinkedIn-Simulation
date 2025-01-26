package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.ArrayList;
import java.util.List;

public class IndexByName {
    private BPlusTree bpt;

    public IndexByName(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        int key = person.getName().hashCode();
        List<User> existingList = this.bpt.search(key);

        if (existingList == null) {
            this.bpt.insert(key, person);
        } else {
            this.bpt.insert(key, person);
        }
    }

    public List<User> searchByName(String name) {
        int key = name.hashCode();
        List<User> result = this.bpt.search(key);
        return result != null ? result : new ArrayList<>();
    }
}
