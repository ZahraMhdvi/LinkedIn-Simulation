import DataStructures.Graph.AdjMapGraph;
import DataStructures.Table.Table;
import File.JsonFileHandler;
import Panel.DataType;
import Panel.UserPanel;
import User.User;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        UserPanel userPanel = new UserPanel();
        userPanel.displayAuthMenu();

    }
}


//---------------------------------------------------------
//این مین برای تست ساخت تیبل برای یوزره پاکش نکن لطفا😘
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        UserPanel userPanel = UserPanel.getUserPanel();
//
//        System.out.println("Enter table name:");
//        String tableName = scanner.nextLine();
//        userPanel.createTable(tableName, scanner);
//
//        System.out.println("Enter table name to insert data:");
//        tableName = scanner.nextLine();
//
//        System.out.println("How many rows do you want to insert?");
//        int rowCount = scanner.nextInt();
//        scanner.nextLine();
//
//        for (int i = 0; i < rowCount; i++) {
//            userPanel.insertIntoTable(tableName, scanner);
//        }
//
//        System.out.println("\nTable after insertion:");
//        userPanel.displayTable(tableName);
//
//        System.out.println("\nEnter ID to delete a row:");
//        int deleteId = scanner.nextInt();
//        userPanel.deleteRowFromTable(tableName, deleteId);
//
//        System.out.println("\nTable after deletion:");
//        userPanel.displayTable(tableName);
//    }
//}
