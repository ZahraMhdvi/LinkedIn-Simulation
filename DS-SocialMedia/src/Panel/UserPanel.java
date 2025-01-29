package Panel;

import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import User.User;

import java.util.List;

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
}
