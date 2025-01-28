import DataStructures.Table.Table;
import User.User;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        List<String> columns = List.of(
                "id", "name", "dateOfBirth", "universityLocation", "field",
                "workplace", "specialties", "connections"
        );

        Table<Integer, User> userTable = new Table<>("UserTable", columns);
        JsonFileHandler fileHandler = new JsonFileHandler();
        fileHandler.constructDefaultTable(userTable);
        userTable.displayTable();
    }
}
