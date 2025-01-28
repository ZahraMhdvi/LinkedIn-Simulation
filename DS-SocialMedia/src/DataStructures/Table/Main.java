package DataStructures.Table;

import User.User;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        List<String> columns = List.of(
                "id", "name", "dateOfBirth", "universityLocation", "field",
                "workplace", "specialties", "connections"
        );

        Table<String, User> userTable = new Table<>("UserTable", columns);

        userTable.insert("Alice", new User(
                1, "Alice", "1990-01-15", "Harvard", "Computer Science", "Google",
                List.of("AI", "Machine Learning", "Cloud Computing"), Set.of(2, 3)
        ));
        userTable.insert("Bob", new User(
                2, "Bob", "1985-06-20", "MIT", "Physics", "NASA",
                List.of("Astrophysics", "Quantum Mechanics"), Set.of(1, 3)
        ));
        userTable.insert("Charlie", new User(
                3, "Charlie", "1992-11-05", "Stanford", "Mathematics", "DeepMind",
                List.of("Algorithms", "Optimization", "Cryptography"), Set.of(1, 2)
        ));
        userTable.insert("Charlie", new User(
                4, "Charlie", "1992-11-05", "californian", "Mathematics", "DeepMind",
                List.of("Algorithms", "Optimization", "Cryptography"), Set.of(3, 2)
        ));

        System.out.println("Initial Table:");
        userTable.displayTable();

        System.out.println("\nAfter Deleting User with ID 2:");
        userTable.delete("Alice");

        userTable.displayTable();
        userTable.search("Alice");
    }
}
