package Panel;

import DataStructures.BPTree.Node;
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
    private static AdjMapGraph<User, Integer> usersGraph;


    public UserPanel() {
        List<String> defaultColumns = List.of(
                "id", "name", "dateOfBirth", "universityLocation", "field",
                "workplace", "specialties", "connections"
        );
        this.userTable = new Table<>("Default User Table", defaultColumns);
        this.fileHandler = new JsonFileHandler();
        usersGraph = new AdjMapGraph<>();
        fileHandler.constructDefaultTable(userTable);
        fileHandler.constructDefaultGraph(usersGraph);
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
        System.out.println();
        System.out.println("1. Login\n2. Sign Up");
    }

    public void displayTableMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Table Management Menu ===");
            System.out.println("1. Create Table");
            System.out.println("2. Insert Row");
            System.out.println("3. Delete Row");
            System.out.println("4. Display Table");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter table name: ");
                    String tableName = scanner.nextLine();
                    getCurrentUser().createTable(tableName, scanner);
                    break;
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
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }


    public void displayUserDetails(int userId) {
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
            if (user.getId() == userId)
                wantedUser = user;
        }
        if (wantedUser != null) {
            int counter = 1;
            for (Entry entry : wantedUser.finalNormalSuggestion(wantedUser)) {
                System.out.println(counter++ + ". " + entry.getValue());
                if (counter == 21)
                    break;
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
                if (counter == 21)
                    break;
            }
        }
    }

}
