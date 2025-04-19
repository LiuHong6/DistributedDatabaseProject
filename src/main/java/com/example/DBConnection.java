package com.example;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Lock timeout setting (milliseconds)
    private static final int BUSY_TIMEOUT_MS = 2000;
    
    public static Connection getConnection() throws SQLException {
        // Use class loader to get the database file from the resource directory
        String url = "jdbc:sqlite::resource:CSCI7785_database.db";
        Connection conn = DriverManager.getConnection(url);
        
        // Set lock timeout
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA busy_timeout = " + BUSY_TIMEOUT_MS);
        stmt.close();
        
        return conn;
    }
    
    // Get a non-auto-commit connection
    public static Connection getTransactionConnection() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        return conn;
    }
    
    // Commit transaction
    public static void commitTransaction(Connection conn) throws SQLException {
        if (conn != null && !conn.getAutoCommit()) {
            conn.commit();
        }
    }
    
    // Rollback transaction
    public static void rollbackTransaction(Connection conn) throws SQLException {
        if (conn != null && !conn.getAutoCommit()) {
            conn.rollback();
        }
    }
    
    // Close connection
    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
