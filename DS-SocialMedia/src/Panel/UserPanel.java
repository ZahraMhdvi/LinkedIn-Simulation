package Panel;

import DataStructures.BPTree.Node;
import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import Panel.Index.IndexByField;
import Panel.Index.IndexByName;
import Panel.Index.IndexByUniversity;
import Panel.Index.IndexByWorkPlace;
import User.User;

import java.util.*;

public class UserPanel {

    private User currentUser;
    private static UserPanel userPanel;
    private Table<Integer, User> userTable;
    private JsonFileHandler fileHandler;
    private static AdjMapGraph<User, Integer> usersGraph;
    private Map<String,Table<String,User>> listOfCustomTables;


    public UserPanel() {
        listOfCustomTables=new HashMap<>();
        List<String> defaultColumns = List.of("id", "name", "dateOfBirth", "universityLocation", "field", "workplace", "specialties", "connections");
        this.userTable = new Table<>("Default User Table", defaultColumns);
        this.fileHandler = new JsonFileHandler();
        usersGraph = new AdjMapGraph<>();
        fileHandler.constructDefaultTable(userTable);
        fileHandler.constructDefaultGraph(usersGraph);
    }

    public static UserPanel getUserPanel() {
        if (userPanel == null) userPanel = new UserPanel();
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
        UserPanel.usersGraph = usersGraph;
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
            if (user.getId() == id) loggedInUser = user;
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
        System.out.println();
        System.out.println("1. Login\n2. Sign Up");
    }

    public void displayTableMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Table Management Menu ===");
            System.out.println("1. Create Table");
            System.out.println("2. Insert Row");
            System.out.println("3. Delete Row");
            System.out.println("4. Display My own Table");
            System.out.println("5. Visit Custom Tables that I indexed");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            String tableName;
            switch (choice) {
                case 1:
                    System.out.println("1. User Table");
                    System.out.println("2. My own Table");
                    System.out.println("Enter your choice: ");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine();
                    if (choice2==1){
                        System.out.println("what do you want index with?");
                        System.out.println("1. Field");
                        System.out.println("2. Name");
                        System.out.println("3. University");
                        System.out.println("4. Workplace");
                        System.out.println("Enter your choice: ");
                        int choice3 = scanner.nextInt();
                        scanner.nextLine();
                        if (choice3==1){
                            indexByField();
                        }
                        else if (choice3==2){
                            indexByName();
                        }
                        else if (choice3==3){
                            indexByUniversity();
                        }
                        else indexByWorkPlace();
                        break;
                    }

                else{
                    System.out.print("Enter table name: ");
                    tableName = scanner.nextLine();
                    getCurrentUser().createTable(tableName, scanner);

                }break;

                case 2:
                    System.out.print("Enter table name: ");
                    tableName = scanner.nextLine();
                    getCurrentUser().insertIntoTable(tableName, scanner);
                    break;
                case 3:
                    System.out.print("Enter table name: ");
                    tableName = scanner.nextLine();
                    System.out.print("Enter row ID to delete: ");
                    int rowId = scanner.nextInt();
                    scanner.nextLine();
                    getCurrentUser().deleteRowFromTable(tableName, rowId);
                    break;
                case 4:
                    System.out.print("Enter table name: ");
                    tableName = scanner.nextLine();
                    getCurrentUser().displayTable(tableName);
                    break;

                case 5:
                    if (listOfCustomTables.isEmpty()) {
                        System.out.println("You didn't create any indexing table");
                        break;
                    }
                else {
                    System.out.println("Which tables do you want to visit ?");
                        for (String s:listOfCustomTables.keySet())
                            System.out.println(s);
                        System.out.println("Enter your choice like (name , field , university): ");
                        String name=scanner.nextLine();
                        listOfCustomTables.get(name).displayTable();
                        break;
                }

                case 6:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }


    public void displayUserDetailsById(int userId) {
        List<Node<Integer, User>> user = userTable.search(userId);

        if (user == null) {
            System.out.println("❌ User not found!");
            return;
        }

        System.out.println("\n=== User Details ===");
        System.out.println(user);
    }

    public void displaySuggestionsForUser(int userId) {
        User wantedUser = null;
        for (User user : getUsersGraph().vertices()) {
            if (user.getId() == userId) wantedUser = user;
        }
        if (wantedUser != null) {
            int counter = 1;
            for (Entry entry : wantedUser.finalNormalSuggestion(wantedUser)) {
                System.out.println(counter++ + ". " + entry.getValue());
                if (counter == 21) break;
            }
        } else System.out.println("User with this ID does not exist!");
    }

    public void displaySuggestionsForCurrentUser() {
        if (getCurrentUser() == null)
            System.out.println("you're not logged in! please try logging in or signing up first.");
        else {
            int counter = 1;
            for (Entry entry : getCurrentUser().finalNormalSuggestion(getCurrentUser())) {
                System.out.println(counter++ + ". " + entry.getValue());
                if (counter == 21) break;
            }
        }
    }


    public void indexByField(){
        IndexByField indexByField=new IndexByField();
        List<String> defaultColumns = List.of("id", "name", "dateOfBirth", "universityLocation", "field", "workplace", "specialties", "connections");
        Table<String,User> fieldTable=new Table<>("field User Table", defaultColumns);
         listOfCustomTables.put("field",fieldTable);
        indexByField.constructCustomTable(fieldTable);
    }

    public void indexByName(){
        IndexByName indexByName=new IndexByName();
        List<String> defaultColumns = List.of("id", "name", "dateOfBirth", "universityLocation", "field", "workplace", "specialties", "connections");
        Table<String,User> nameTable=new Table<>("name User Table", defaultColumns);
        listOfCustomTables.put("name",nameTable);
        indexByName.constructCustomTable(nameTable);
    }

    public void indexByUniversity(){
        IndexByUniversity indexByUniversity=new IndexByUniversity();
        List<String> defaultColumns = List.of("id", "name", "dateOfBirth", "universityLocation", "field", "workplace", "specialties", "connections");
        Table<String,User> universityTable=new Table<>("university User Table", defaultColumns);
        listOfCustomTables.put("university",universityTable);
        indexByUniversity.constructCustomTable(universityTable);
    }


    public void indexByWorkPlace(){
        IndexByWorkPlace indexByWorkPlace=new IndexByWorkPlace();
        List<String> defaultColumns = List.of("id", "name", "dateOfBirth", "universityLocation", "field", "workplace", "specialties", "connections");
        Table<String,User> workplaceUserTable=new Table<>("workplace User Table", defaultColumns);
        listOfCustomTables.put("workplace",workplaceUserTable);
        indexByWorkPlace.constructCustomTable(workplaceUserTable);
    }


    public void searchByCustomIndex(int choice){
        Scanner sc=new Scanner(System.in);
        String ch;
        switch (choice){
            case 2:
                indexByField();
                System.out.println("Enter the field that you want to search: ");
                ch=sc.nextLine();
                for (Node<String,User> s:listOfCustomTables.get("field").search(ch)){
                    System.out.println(s);
                }break;
                case 3:
                    indexByUniversity();
                    System.out.println("Enter the name of university that you want to search: ");
                    ch=sc.nextLine();
                    for (Node<String,User> s:listOfCustomTables.get("university").search(ch)){
                        System.out.println(s);
                    }break;
            case 4:
                indexByName();
                System.out.println("Enter the name that you want to search: ");
                ch=sc.nextLine();
                for (Node<String,User> s:listOfCustomTables.get("name").search(ch)){
                    System.out.println(s);
                }break;
            case 5:
                indexByWorkPlace();
                System.out.println("Enter the name of workplace that you want to search: ");
                ch=sc.nextLine();
                for (Node<String,User> s:listOfCustomTables.get("workplace").search(ch)){
                    System.out.println(s);
                }break;


        }
    }

}
