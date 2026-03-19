package com.srms.ui;

import com.srms.config.DBConnection;
import com.srms.model.User;
import com.srms.service.AuthService;
import com.srms.util.Utils;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main entry point – shows login prompt then routes to role-specific menu.
 */
public class MainMenu {

    private static final Scanner    sc   = new Scanner(System.in);
    private static final AuthService auth = new AuthService();

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DBConnection::close));

        Utils.printBanner("STUDENT RESULT MANAGEMENT SYSTEM");
        System.out.println("  Java + MySQL | JDBC | OOP | SOLID");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("  [1] Login");
            System.out.println("  [0] Exit");
            System.out.print ("  > ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> doLogin();
                case "0" -> running = false;
                default  -> System.out.println("  Invalid choice.");
            }
        }

        System.out.println("\n  Goodbye!");
        DBConnection.close();
    }

    // ── Login flow ────────────────────────────────────────────────────

    private static void doLogin() {
        System.out.print("  Username : "); String username = sc.nextLine().trim();
        System.out.print("  Password : "); String password = sc.nextLine().trim();

        try {
            if (auth.login(username, password)) {
                User u = auth.getCurrentUser();
                System.out.println("\n  Welcome, " + u.getFullName() + "! [" + u.getRole() + "]");
                if (u.isAdmin())   new AdminMenu  (sc, auth).show();
                else               new FacultyMenu(sc, auth).show();
            } else {
                System.out.println("  ✖ Invalid username or password.");
            }
        } catch (SQLException e) {
            System.err.println("  DB Error: " + e.getMessage());
        }
    }
}
