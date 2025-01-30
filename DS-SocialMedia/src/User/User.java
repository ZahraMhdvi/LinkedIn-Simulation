package User;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import Panel.DataType;
import Panel.DynamicTable;
import Panel.Entry;
import Panel.UserPanel;

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
    private ArrayList<Entry> normalSuggestedUsers;
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

    public ArrayList<Entry> getNormalSuggestedUsers() {
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
        this.normalSuggestedUsers = new ArrayList<>();
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

    public ArrayList<Entry> finalNormalSuggestion(User user) {
        if (user.getNormalSuggestedUsers().isEmpty() || user.hasEditedConnections) {
            user.getNormalSuggestedUsers().clear();
            ArrayList<Entry> bfsMap = new ArrayList<>();
            fillBFSMap(bfsMap, user);
            updateScores(user, bfsMap);
            for (Entry entry : bfsMap) {
                if (UserPanel.getUserPanel().getUsersGraph().getEdge(user, entry.getValue()) == null && !entry.getValue().equals(user))
                    user.getNormalSuggestedUsers().add(entry);
            }
            sortSuggestedList(user.getNormalSuggestedUsers());
        }
        return user.getNormalSuggestedUsers();
    }

    private void sortSuggestedList(ArrayList<Entry> suggestedUsers) {
        suggestedUsers.sort(Comparator.comparingInt(Entry::getKey).reversed());
    }

    private void updateScores(User user, ArrayList<Entry> bfsMap) {
        List<Entry> entriesToUpdate = new ArrayList<>(bfsMap);
        for (Entry entry : entriesToUpdate) {
            int newScore = calculateScore(entry, user);
            int prevScore = entry.getKey();
            User temp = entry.getValue();
            bfsMap.remove(entry);
            bfsMap.add(new Entry(prevScore + newScore, temp));
        }
    }

    private int calculateScore(Entry user, User currentUser) {
        int score = 0;
        score = fieldScore(user, currentUser, score);
        score = workScore(user, currentUser, score);
        score = universityScore(user, currentUser, score);
        score = specialityScore(user, currentUser, score);
        return score;
    }

    private static int universityScore(Entry user, User currentUser, int score) {
        if (user.getValue().getUniversityLocation().equalsIgnoreCase(currentUser.getUniversityLocation()))
            score++;
        return score;
    }

    private static int workScore(Entry user, User currentUser, int score) {
        if (user.getValue().getWorkplace().equalsIgnoreCase(currentUser.getWorkplace()))
            score++;
        return score;
    }

    private static int fieldScore(Entry user, User currentUser, int score) {
        if (user.getValue().getField().equalsIgnoreCase(currentUser.getField()))
            score++;
        return score;
    }

    private static int specialityScore(Entry user, User currentUser, int score) {
        for (String speciality : currentUser.getSpecialties()) {
            if (user.getValue().getSpecialties().contains(speciality))
                score++;
        }
        return score;
    }


    private void fillBFSMap(ArrayList<Entry> bfsMap, User currentUser) {
        Set<User> levelZero = UserPanel.getUserPanel().getUsersGraph().getNeighbors(currentUser);
        Set<User> firstLevelSet = firstLevelBFS(bfsMap, currentUser, levelZero);
        secondLevelBFS(bfsMap, currentUser, firstLevelSet);
    }

    private static void secondLevelBFS(ArrayList<Entry> bfsMap, User currentUser, Set<User> firstLevelSet) {
        Set<User> secondLevelSet = new HashSet<>();
        for (User neighbor : firstLevelSet) {
            secondLevelSet.addAll(UserPanel.getUserPanel().getUsersGraph().getNeighbors(neighbor));
        }
        secondLevelSet.remove(currentUser);
        for (User neighbor2 : secondLevelSet) {
            if (!firstLevelSet.contains(neighbor2))
                bfsMap.add(new Entry(1, neighbor2));
        }
    }

    private static Set<User> firstLevelBFS(ArrayList<Entry> bfsMap, User currentUser, Set<User> levelZero) {
        Set<User> firstLevelSet = new HashSet<>();
        for (User neighbor : levelZero) {
            firstLevelSet.addAll(UserPanel.getUserPanel().getUsersGraph().getNeighbors(neighbor));
        }
        firstLevelSet.remove(currentUser);
        for (User neighbor1 : firstLevelSet) {
            bfsMap.add(new Entry(2, neighbor1));
        }
        return firstLevelSet;
    }

    public ArrayList<Entry> customSuggestion(User user, Map<String, Integer> priorityWeights) {
        if (user.getNormalSuggestedUsers().isEmpty() || user.hasEditedConnections) {
            user.getNormalSuggestedUsers().clear();
            ArrayList<Entry> bfsMap = new ArrayList<>();
            fillBFSMap(bfsMap, user);
            updateCustomScores(user, bfsMap, priorityWeights);

            bfsMap.removeIf(entry -> user.getConnections().contains(entry.getValue().getId()));

            sortSuggestedList(bfsMap);

            int suggestionLimit = Math.min(20, bfsMap.size());
            user.getNormalSuggestedUsers().addAll(bfsMap.subList(0, suggestionLimit));
        }
        return user.getNormalSuggestedUsers();
    }

    private void updateCustomScores(User user, ArrayList<Entry> bfsMap, Map<String, Integer> priorityWeights) {
        for (Entry entry : bfsMap) {
            int customScore = calculateCustomScore(entry, user, priorityWeights);
            entry.setKey(customScore);
        }
    }

    private int calculateCustomScore(Entry entry, User currentUser, Map<String, Integer> priorityWeights) {
        int score = 0;
        score += weightedUniversityScore(entry, currentUser, priorityWeights.getOrDefault("universityLocation", 1));
        score += weightedWorkScore(entry, currentUser, priorityWeights.getOrDefault("workplace", 1));
        score += weightedFieldScore(entry, currentUser, priorityWeights.getOrDefault("field", 1));
        score += weightedSpecialityScore(entry, currentUser, priorityWeights.getOrDefault("specialties", 1));
        return score;
    }


    private int weightedUniversityScore(Entry entry, User currentUser, int weight) {
        return entry.getValue().getUniversityLocation().equalsIgnoreCase(currentUser.getUniversityLocation()) ? weight : 0;
    }

    private int weightedWorkScore(Entry entry, User currentUser, int weight) {
        if(entry.getValue().getWorkplace().equalsIgnoreCase(currentUser.getWorkplace()))
            return weight;
        else return 0;
    }

    private int weightedFieldScore(Entry entry, User currentUser, int weight) {
        if (entry.getValue().getField().equalsIgnoreCase(currentUser.getField()))
            return weight;
        return 0;
    }

    private int weightedSpecialityScore(Entry entry, User currentUser, int weight) {
        long specialityMatchCount = currentUser.getSpecialties().stream().filter(specialty -> entry.getValue().getSpecialties().contains(specialty)).count();
        return (int) (specialityMatchCount * weight);
    }

}
