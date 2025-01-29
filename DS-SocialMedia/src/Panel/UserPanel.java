package Panel;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import User.User;

import java.util.*;

public class UserPanel {

    private User currentUser;
    private static UserPanel userPanel;
    private Table<Integer, User> userTable;
    private JsonFileHandler fileHandler;
    private AdjMapGraph<User, Integer> usersGraph;
    private Map<String, DynamicTable<Integer, Object>> tables;


    public UserPanel() {
        List<String> defaultColumns = List.of(
                "id", "name", "dateOfBirth", "universityLocation", "field",
                "workplace", "specialties", "connections"
        );
        this.userTable = new Table<>("Default User Table", defaultColumns);
        this.fileHandler = new JsonFileHandler();
        this.usersGraph = new AdjMapGraph<>();
        fileHandler.constructDefaultTable(userTable);
        fileHandler.constructDefaultGraph(usersGraph);
        this.tables=new HashMap<>();
    }

    public static UserPanel getUserPanel() {
        if (userPanel == null)
            userPanel = new UserPanel();
        return userPanel;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public static void setUserPanel(UserPanel userPanel) {
        UserPanel.userPanel = userPanel;
    }

    public Table<Integer, User> getUserTable() {
        return userTable;
    }

    public void setUserTable(Table<Integer, User> userTable) {
        this.userTable = userTable;
    }

    public JsonFileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(JsonFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public AdjMapGraph<User, Integer> getUsersGraph() {
        return usersGraph;
    }

    public void setUsersGraph(AdjMapGraph<User, Integer> usersGraph) {
        this.usersGraph = usersGraph;
    }

    public void signUp(String name, String dateOfBirth, String universityLocation, String field, String workplace, List<String> specialties, Set<Integer> connections) {
        User newUser = new User(getUsersGraph().numVertices() + 1, name, dateOfBirth, universityLocation, field, workplace, specialties, connections);
        setCurrentUser(newUser);
        getUserTable().insert(newUser.getId(), newUser);
        getUsersGraph().insertVertex(newUser);
        getFileHandler().addUserToJson(newUser);
    }

    public void logout() {
        if (getCurrentUser() == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
        setCurrentUser(null);
        System.out.println("logging out was successful!");
    }

    public void login(int id, String name) {
        User loggedInUser = null;
        for (User user : getUsersGraph().vertices()) {
            if (user.getId() == id)
                loggedInUser = user;
        }
        if (loggedInUser == null) {
            System.out.println("User not found. try logging in again or signing up.");
            return;
        } else if (!loggedInUser.getName().equalsIgnoreCase(name)) {
            System.out.println("Wrong name!");
            return;
        }
        setCurrentUser(loggedInUser);
    }

    public void displayAuthMenu() {
        System.out.println();
        System.out.println("\033[94m" + "Connect, " + "\033[0m" + "Grow, " + "\033[94m" + "Succeed" + "\033[0m" + "!\nYour Network, " + "\033[94m" + "Your Power!" + "\033[0m");
        System.out.println();
        System.out.println("Welcome!");
    }






    public void createTable(String tableName, Scanner scanner) {
        if (tables.containsKey(tableName)) {
            System.out.println("❌Table already exists!");
            return;
        }

        System.out.println("Enter number of columns:");
        int columnCount = scanner.nextInt();
        scanner.nextLine();

        Map<String, DataType> columnDataTypes = new LinkedHashMap<>();

        for (int i = 0; i < columnCount; i++) {
            System.out.println("Enter column name " + (i + 1) + ":");
            String columnName = scanner.nextLine();

            System.out.println("Enter data type for \"" + columnName + "\" (INTEGER, DOUBLE, FLOAT, STRING, DATE):");
            String dataTypeStr = scanner.nextLine().toUpperCase();

            DataType dataType;
            try {
                dataType = DataType.valueOf(dataTypeStr);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid data type! Using STRING by default.");
                dataType = DataType.STRING;
            }

            columnDataTypes.put(columnName, dataType);
        }

        tables.put(tableName, new DynamicTable<>(tableName, columnDataTypes));
        System.out.println("✅Table \"" + tableName + "\" created successfully!");
    }

    public void insertIntoTable(String tableName, Scanner scanner) {
        DynamicTable<Integer, Object> table = tables.get(tableName);

        if (table == null) {
            System.out.println("❌ Table not found!");
            return;
        }

        int rowId = table.getNextId();
        Map<String, Object> rowData = new LinkedHashMap<>();

        System.out.println("\nInserting row with ID: " + rowId);
        for (Map.Entry<String, DataType> entry : table.getColumnDataTypes().entrySet()) {
            String columnName = entry.getKey();
            DataType dataType = entry.getValue();
            System.out.println("Enter value for " + columnName + " (" + dataType + "):");

            Object value = getInputValue(scanner, dataType);
            rowData.put(columnName, value);
        }

        table.insert(rowId, rowData);
        System.out.println("✅ Row inserted successfully!");
    }

    public void deleteRowFromTable(String tableName, int id) {
        DynamicTable<Integer, Object> table = tables.get(tableName);

        if (table == null) {
            System.out.println("❌ Table not found!");
            return;
        }

        table.delete(id);
        System.out.println("✅ Row with ID " + id + " deleted successfully!");
    }

    public void displayTable(String tableName) {
        DynamicTable<Integer, Object> table = tables.get(tableName);

        if (table == null) {
            System.out.println("❌ Table not found!");
            return;
        }

        table.displayTable();
    }

    private Object getInputValue(Scanner scanner, DataType dataType) {
        switch (dataType) {
            case INTEGER:
                return scanner.nextInt();
            case DOUBLE:
                return scanner.nextDouble();
            case FLOAT:
                return scanner.nextFloat();
            case STRING:
                return scanner.next();
            case DATE:
                System.out.println("Enter date (YYYY-MM-DD):");
                return scanner.next();
            default:
                return null;
        }
    }
}
