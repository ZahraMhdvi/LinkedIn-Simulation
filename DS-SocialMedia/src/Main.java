import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import User.User;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //TODO: move all these to a particular class
        // to handle constructing default DB and graph then show user's menu
        List<String> columns = List.of(
                "id", "name", "dateOfBirth", "universityLocation", "field",
                "workplace", "specialties", "connections"
        );
        Table<Integer, User> userTable = new Table<>("UserTable", columns);
        JsonFileHandler fileHandler = new JsonFileHandler();
        AdjMapGraph<User, Integer> usersGraph = new AdjMapGraph<>();
        fileHandler.constructDefaultTable(userTable);
        //userTable.displayTable();
        fileHandler.constructDefaultGraph(usersGraph);
        /*User us = fileHandler.getInitialMap().get(3);
        us.deleteExistingConnection(fileHandler.getInitialMap().get(42), usersGraph, fileHandler, userTable);
        userTable.displayTable();*/
    }
}
