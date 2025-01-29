package Panel;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import User.User;

import java.util.List;
import java.util.Set;

public class UserPanel {

    private User currentUser;
    private static UserPanel userPanel;
    private Table<Integer, User> userTable;
    private JsonFileHandler fileHandler;
    private AdjMapGraph<User, Integer> usersGraph;

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
}
