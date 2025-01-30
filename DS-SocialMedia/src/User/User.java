package User;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import Panel.DataType;
import Panel.DynamicTable;

import java.util.*;

public class User {
    private int id;
    private String name;
    private String dateOfBirth;
    private String universityLocation;
    private String field;
    private String workplace;
    private List<String> specialties;
    private Set<Integer> connections;
    private Map<String, DynamicTable<Integer, Object>> tables;
    private Map<Integer, User> normalSuggestedUsers;
    private boolean hasEditedConnections;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUniversityLocation() {
        return universityLocation;
    }

    public void setUniversityLocation(String universityLocation) {
        this.universityLocation = universityLocation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public Set<Integer> getConnections() {
        return connections;
    }

    public void setConnections(Set<Integer> connections) {
        this.connections = connections;
    }

    public Map<Integer, User> getNormalSuggestedUsers() {
        return normalSuggestedUsers;
    }

    public boolean getEditedConnections() {
        return hasEditedConnections;
    }

    public void setEditedConnections(boolean editedConnections) {
        this.hasEditedConnections = editedConnections;
    }

    public User(int id, String name, String dateOfBirth, String universityLocation, String field, String workplace, List<String> specialties, Set<Integer> connections) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.universityLocation = universityLocation;
        this.field = field;
        this.workplace = workplace;
        this.specialties = specialties;
        this.connections = new HashSet<>(connections);
        this.tables=new HashMap<>();
        this.normalSuggestedUsers = new HashMap<>();
        this.hasEditedConnections = false;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", universityLocation='" + universityLocation + '\'' +
                ", field='" + field + '\'' +
                ", workplace='" + workplace + '\'' +
                ", specialties=" + specialties +
                ", connections=" + connections +
                '}';
    }

    public void addNewConnection(User user, AdjMapGraph<User, Integer> graph, JsonFileHandler fileHandler, Table<Integer, User> table) {
        this.connections.add(user.id);
        user.connections.add(this.id);
        graph.insertEdge(this, user, JsonFileHandler.totalEdgesCounter++);
        fileHandler.writeConnectionInJson(getId(), user.getId());
        table.delete(this.id);
        table.delete(user.id);
        table.insert(this.id, this);
        table.insert(user.id, user);
        setEditedConnections(true);
    }

    public void deleteExistingConnection(User user, AdjMapGraph<User, Integer> graph, JsonFileHandler fileHandler, Table<Integer, User> table) {
        this.connections.remove(user.id);
        user.connections.remove(this.id);
        graph.removeEdge(graph.getEdge(this, user));
        fileHandler.deleteConnectionInJson(getId(), user.getId());
        table.delete(this.id);
        table.delete(user.id);
        table.insert(this.id, this);
        table.insert(user.id, user);
        setEditedConnections(true);
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

    public Map<Integer, User> finalNormalSuggestion(User user) {
        if (user.getNormalSuggestedUsers().isEmpty() || user.hasEditedConnections) {
            //todo: suggest
        }
        return user.getNormalSuggestedUsers();
    }
}
