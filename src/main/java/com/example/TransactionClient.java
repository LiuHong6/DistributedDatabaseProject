package com.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.Random;
import java.util.List;

public class TransactionClient implements Runnable {
    private final String clientId;
    private final EMPDAO empDAO;
    private final CountDownLatch latch;
    private final Random random = new Random();
    private final String targetEmployeeId; // Target conflict employee ID
    
    public TransactionClient(String clientId, EMPDAO empDAO, CountDownLatch latch, String targetEmployeeId) {
        this.clientId = clientId;
        this.empDAO = empDAO;
        this.latch = latch;
        this.targetEmployeeId = targetEmployeeId;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Client " + clientId + " started");
            latch.await(); // Wait for all clients to be ready
            
            // Create conflicts: all clients try to operate on the same data
            Connection conn = null;
            try {
                conn = DBConnection.getTransactionConnection();
                
                // Each client randomly selects an operation type, but all target the same record
                int operation = random.nextInt(3); // 0: update, 1: delete, 2: query+update
                
                switch (operation) {
                    case 0: // Update
                        String newName = "Updated Employee" + clientId;
                        String newTitle = "New Position" + clientId;
                        System.out.println("Client " + clientId + " attempting to update: " + targetEmployeeId + " -> " + newName + ", " + newTitle);
                        
                        // First perform some delay operations to increase the possibility of conflicts
                        Thread.sleep(random.nextInt(100)); 
                        
                        empDAO.updateEmployeeWithTransaction(conn, targetEmployeeId, newName, newTitle);
                        System.out.println("Client " + clientId + " update operation successful, waiting for commit");
                        
                        // Add more delay to increase the possibility of conflicts
                        Thread.sleep(random.nextInt(300) + 100);
                        break;
                        
                    case 1: // Delete and then insert (first delete the shared record, then insert a new record)
                        System.out.println("Client " + clientId + " attempting to delete and reinsert: " + targetEmployeeId);
                        
                        // Delay operation
                        Thread.sleep(random.nextInt(100));
                        
                        // Delete first
                        empDAO.deleteEmployeeWithTransaction(conn, targetEmployeeId);
                        System.out.println("Client " + clientId + " delete operation successful");
                        
                        Thread.sleep(random.nextInt(200));
                        
                        // Then insert a new record with the same ID
                        empDAO.addNewEmployeeWithTransaction(conn, targetEmployeeId, "New Employee" + clientId, "New Position" + random.nextInt(5));
                        System.out.println("Client " + clientId + " reinsert operation successful, waiting for commit");
                        
                        Thread.sleep(random.nextInt(200));
                        break;
                        
                    case 2: // Read then update (first read the data, then make modifications)
                        System.out.println("Client " + clientId + " attempting to read then update: " + targetEmployeeId);
                        
                        // First read the record (this will acquire a read lock)
                        List<EMP> employees = empDAO.getAllEmployeesWithTransaction(conn);
                        System.out.println("Client " + clientId + " query operation successful, found " + employees.size() + " records");
                        
                        // Delay for a period of time, simulating data processing
                        Thread.sleep(random.nextInt(300) + 200);
                        
                        // Then try to update (this requires a write lock)
                        String updatedName = "Read-then-Update" + clientId;
                        String updatedTitle = "Read-then-Position" + clientId;
                        empDAO.updateEmployeeWithTransaction(conn, targetEmployeeId, updatedName, updatedTitle);
                        System.out.println("Client " + clientId + " read-then-update operation successful, waiting for commit");
                        
                        Thread.sleep(random.nextInt(200));
                        break;
                }
                
                try {
                    // Try to commit the transaction
                    System.out.println("Client " + clientId + " attempting to commit transaction");
                    DBConnection.commitTransaction(conn);
                    System.out.println("Client " + clientId + " successfully committed transaction");
                } catch (SQLException e) {
                    // If a conflict occurs during commit, rollback the transaction
                    System.out.println("Client " + clientId + " conflict occurred during commit: " + e.getMessage());
                    DBConnection.rollbackTransaction(conn);
                    System.out.println("Client " + clientId + " transaction rolled back");
                }
                
            } catch (SQLException e) {
                // Catch possible database lock or conflict errors
                System.out.println("Client " + clientId + " conflict occurred during operation: " + e.getMessage());
                if (conn != null) {
                    try {
                        DBConnection.rollbackTransaction(conn);
                        System.out.println("Client " + clientId + " transaction rolled back");
                    } catch (SQLException ex) {
                        System.out.println("Client " + clientId + " error during transaction rollback: " + ex.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Client " + clientId + " other error occurred: " + e.getMessage());
                if (conn != null) {
                    DBConnection.rollbackTransaction(conn);
                }
            } finally {
                if (conn != null) {
                    DBConnection.closeConnection(conn);
                }
            }
            
            System.out.println("Client " + clientId + " completed");
            
        } catch (Exception e) {
            System.out.println("Client " + clientId + " exception occurred: " + e.getMessage());
        }
    }
} 