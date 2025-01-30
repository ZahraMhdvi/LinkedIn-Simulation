package Panel;

import java.util.*;
import User.User;

public class MainMenu {

    private static Scanner scanner = new Scanner(System.in);
    private static UserPanel userPanel = UserPanel.getUserPanel();



    public static void displayAuthMenu() {
        while (true) {
            System.out.println("\n\033[94m" + "Connect, Grow, Succeed!" + "\033[0m");
            System.out.println("\nWelcome!");
            System.out.println("\n1. Login\n2. Sign Up\n3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleSignUp();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Enter your ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        userPanel.login(id, name);

        if (userPanel.getCurrentUser() != null) {
            System.out.println("✅ Login successful!");
            displayMainMenu();
        } else {
            System.out.println("❌ Login failed. Please try again.");
        }
    }

    private static void handleSignUp() {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your date of birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter your university location: ");
        String universityLocation = scanner.nextLine();
        System.out.print("Enter your field of study: ");
        String field = scanner.nextLine();
        System.out.print("Enter your workplace: ");
        String workplace = scanner.nextLine();

        System.out.print("Enter your specialties (comma-separated): ");
        List<String> specialties = Arrays.asList(scanner.nextLine().split(","));

        userPanel.signUp(name, dateOfBirth, universityLocation, field, workplace, specialties, new HashSet<>());
        System.out.println("✅ Sign-up successful! Please login.");
    }

    public static void displayMainMenu() {
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. View Normal Suggestions");
            System.out.println("2. View Custom Suggestions");
            System.out.println("3. View Suggestions for Specific User");
            System.out.println("4. Manage Connections");
            System.out.println("5. Manage Tables");
            System.out.println("6. View User List");
            System.out.println("7. View User Details");
            System.out.println("8. Logout");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    userPanel.displaySuggestionsForCurrentUser();
                    break;
                case 2:
                    displayCustomSuggestions();
                    break;
                case 3:
                    displaySuggestionsForUser();
                    break;
                case 4:
                    manageConnections();
                    break;
                case 5:
                    userPanel.displayTableMenu(scanner);
                    break;
                case 6:
                    displayUserList();
                    break;
                case 7:
                    System.out.print("Enter user ID: ");
                    int userId = scanner.nextInt();
                    userPanel.displayUserDetails(userId);
                    break;
                case 8:
                    userPanel.logout();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void displayUserList() {
        System.out.println("\n=== User List ===");
        userPanel.getUserTable().displayTable();
    }

    private static void displayCustomSuggestions() {
        User currentUser = userPanel.getCurrentUser();
        if (currentUser == null) {
            System.out.println("❌ You need to log in first.");
            return;
        }
        Map<String, Integer> priorityWeights = new HashMap<>();
        System.out.println("\nAssign priority weights (higher value = more importance, enter 0 to ignore):");
        System.out.print("Field of Study: ");
        priorityWeights.put("field", scanner.nextInt());
        System.out.print("Workplace: ");
        priorityWeights.put("workplace", scanner.nextInt());
        System.out.print("University Location: ");
        priorityWeights.put("universityLocation", scanner.nextInt());
        System.out.print("Specialties: ");
        priorityWeights.put("specialties", scanner.nextInt());

        // نمایش پیشنهادات سفارشی
        System.out.println("\n=== Custom Suggestions for User ===");
        ArrayList<Entry> suggestions =
                userPanel.getCurrentUser().customSuggestion(userPanel.getCurrentUser(), priorityWeights);

        if (suggestions.isEmpty()) {
            System.out.println("No suggestions found based on your preferences.");
        } else {
            int counter = 1;
            for (Entry entry : suggestions) {
                System.out.println(counter++ + ". " + entry.getValue().getName()+" -> ID: "+entry.getValue().getId() + " (Score: " + entry.getKey() + ")");
            }
        }
    }


    private static void displaySuggestionsForUser() {
        System.out.print("Enter user ID for suggestions: ");
        int userId = scanner.nextInt();
        userPanel.displaySuggestionsForUser(userId);
    }

    private static void manageConnections() {
        while (true) {
            System.out.println("\n=== Manage Connections ===");
            System.out.println("1. Add Connection");
            System.out.println("2. Remove Connection");
            System.out.println("3. Back to Main Menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addConnection();
                    break;
                case 2:
                    removeConnection();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addConnection() {
        System.out.print("Enter the ID of the user to connect with: ");
        int userId = scanner.nextInt();

        User targetUser = findUserById(userId);
        if (targetUser != null) {
            userPanel.getCurrentUser().addNewConnection(targetUser, userPanel.getUsersGraph(), userPanel.getFileHandler(), userPanel.getUserTable());
            System.out.println("✅ Connection added successfully!");
        } else {
            System.out.println("❌ User not found.");
        }
    }

    private static void removeConnection() {
        System.out.print("Enter the ID of the user to remove from connections: ");
        int userId = scanner.nextInt();

        User targetUser = findUserById(userId);
        if (targetUser != null) {
            userPanel.getCurrentUser().deleteExistingConnection(targetUser, userPanel.getUsersGraph(), userPanel.getFileHandler(), userPanel.getUserTable());
            System.out.println("✅ Connection removed successfully!");
        } else {
            System.out.println("❌ User not found.");
        }
    }

    private static User findUserById(int userId) {
        for (User user : userPanel.getUsersGraph().vertices()) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }
}
