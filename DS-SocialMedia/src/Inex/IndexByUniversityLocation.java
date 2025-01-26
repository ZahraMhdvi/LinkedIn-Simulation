package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexByUniversityLocation {
    private BPlusTree bpt;

    public IndexByUniversityLocation(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        int key = person.getUniversityLocation().hashCode();
        this.bpt.insert(key, person);
    }

    public List<User> searchByUniversityLocation(String universityLocation) {
        int key = universityLocation.hashCode();
        return this.bpt.search(key);
    }
}
