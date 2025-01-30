package Panel;

import java.util.*;

public class Menu {

    private Scanner scanner;
    private UserPanel userPanel;

    public Menu() {
        scanner = new Scanner(System.in);
        userPanel = UserPanel.getUserPanel();
    }

    public void displayMainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Manage Tables");
            System.out.println("5. Manage Users");
            System.out.println("6. View Suggestions");
            System.out.println("7. Manage Connections");
            System.out.println("8. Manage Database");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    signUpUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    logoutUser();
                    break;
                case 4:
                    userPanel.displayTableMenu(scanner);
                    break;
                case 5:
                    displayUserManagementMenu();
                    break;
                case 6:
                    displaySuggestionsMenu();
                    break;
                case 7:
                    displayConnectionManagementMenu();
                    break;
                case 8:
                    displayDatabaseManagementMenu();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void signUpUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter university location: ");
        String universityLocation = scanner.nextLine();
        System.out.print("Enter field: ");
        String field = scanner.nextLine();
        System.out.print("Enter workplace: ");
        String workplace = scanner.nextLine();
        System.out.print("Enter specialties (comma separated): ");
        List<String> specialties = Arrays.asList(scanner.nextLine().split(","));
        System.out.print("Enter connection IDs (comma separated): ");
        Set<Integer> connections = new HashSet<>();
        for (String id : scanner.nextLine().split(",")) {
            connections.add(Integer.parseInt(id.trim()));
        }
        userPanel.signUp(name, dateOfBirth, universityLocation, field, workplace, specialties, connections);
    }

    private void loginUser() {
        System.out.print("Enter user ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        userPanel.login(id, name);
    }

    private void logoutUser() {
        userPanel.logout();
    }

    private void displayUserManagementMenu() {
        while (true) {
            System.out.println("\n=== User Management ===");
            System.out.println("1. Add User");
            System.out.println("2. View User List");
            System.out.println("3. View User Details");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    signUpUser();
                    break;
                case 2:
                    userPanel.displayUserList();
                    break;
                case 3:
                    System.out.print("Enter user ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    userPanel.displayUserDetails(id);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void displaySuggestionsMenu() {
        while (true) {
            System.out.println("\n=== Suggestions ===");
            System.out.println("1. View Suggestions for Current User");
            System.out.println("2. View Suggestions for Another User");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    userPanel.displaySuggestionsForCurrentUser();
                    break;
                case 2:
                    System.out.print("Enter user ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    userPanel.displaySuggestionsForUser(id);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void displayConnectionManagementMenu() {
        while (true) {
            System.out.println("\n=== Connection Management ===");
            System.out.println("1. Connect to User");
            System.out.println("2. Disconnect from User");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter your user ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter user ID to connect to: ");
                    int targetUserId = scanner.nextInt();
                    scanner.nextLine();
                    //TODO add new connection
                    break;
                case 2:
                    System.out.print("Enter your user ID: ");
                    userId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter user ID to disconnect from: ");
                    targetUserId = scanner.nextInt();
                    scanner.nextLine();
                    //TODO disconnecting
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void displayDatabaseManagementMenu() {
        while (true) {
            System.out.println("\n=== Database Management ===");
            System.out.println("1. Create Table");
            System.out.println("2. Add Record");
            System.out.println("3. Delete Record");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter table name: ");
                    String tableName = scanner.nextLine();
                    userPanel.getCurrentUser().createTable(tableName, scanner);
                    break;
                case 2:
                    System.out.print("Enter table name: ");
                    tableName = scanner.nextLine();
                    userPanel.getCurrentUser().insertIntoTable(tableName, scanner);
                    break;
                case 3:
                    System.out.print("Enter table name: ");
                    tableName = scanner.nextLine();
                    System.out.print("Enter row ID to delete: ");
                    int rowId = scanner.nextInt();
                    scanner.nextLine();
                    userPanel.getCurrentUser().deleteRowFromTable(tableName, rowId);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.displayMainMenu();
    }
}