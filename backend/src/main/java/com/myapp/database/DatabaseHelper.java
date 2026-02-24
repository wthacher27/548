package com.myapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    // Format: jdbc:mysql://[host]:[port]/[database_name]
    private static final String URL = "jdbc:mysql://localhost:3306/WorkoutTracker";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = "M0lly!sCut3"; // Your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}