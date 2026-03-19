package com.srms.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton database connection manager using JDBC.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/student_result_ms?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";        // Change to your MySQL username
    private static final String PASSWORD = "your_password"; // Change to your MySQL password

    private static Connection connection = null;

    private DBConnection() {}

    /**
     * Returns the single shared connection (creates one if not yet open).
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j.jar to /lib", e);
            }
        }
        return connection;
    }

    /** Close the connection when the application exits. */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing DB connection: " + e.getMessage());
        }
    }
}
