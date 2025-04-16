package com.example;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        // Use a relative path to the SQLite database file in the project root
        String url = "jdbc:sqlite:CSCI7785_database.db";
        return DriverManager.getConnection(url);
    }
}
