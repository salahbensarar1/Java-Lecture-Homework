package org.example.javalecturehomework.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:identifier.sqlite";
    private DatabaseConnection() {}
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

}
