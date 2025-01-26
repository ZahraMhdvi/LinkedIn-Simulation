package src.Inex;

import src.DataStructures.BPTree.BPlusTree;
import src.User.User;

import java.util.List;

public class IndexByConnections {
    private BPlusTree bpt;

    public IndexByConnections(int order) {
        this.bpt = new BPlusTree(order);
    }

    public void insert(User person) {
        for (Integer connectionId : person.getConnections()) {
            this.bpt.insert(connectionId, person);
        }
    }

    public List<User> searchByConnection(int connectionId) {
        return this.bpt.search(connectionId, connectionId);
    }
}
