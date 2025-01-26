package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexById {
    private BPlusTree bpt;

    public IndexById(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        this.bpt.insert(person.getId(), person);
    }

    public List<User> searchById(int id) {
        return this.bpt.search(id);
    }
}
