package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexBySpecialties {
    private BPlusTree bpt;

    public IndexBySpecialties(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        for (String specialty : person.getSpecialties()) {
            int key = specialty.hashCode();


            this.bpt.insert(key, person);
        }
    }

    public List<User> searchBySpecialty(String specialty) {
        int key = specialty.hashCode();

        List<User> result = this.bpt.search(key);

        return result != null ? result : List.of();
    }
}
