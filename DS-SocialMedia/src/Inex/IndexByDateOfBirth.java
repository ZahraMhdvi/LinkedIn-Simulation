package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexByDateOfBirth {
    private BPlusTree bpt;

    public IndexByDateOfBirth(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        int key = person.getDateOfBirth().hashCode();
        this.bpt.insert(key, person);
    }

    public List<User> searchByDateOfBirth(String dateOfBirth) {
        int key = dateOfBirth.hashCode();
        return this.bpt.search(key);
    }
}
