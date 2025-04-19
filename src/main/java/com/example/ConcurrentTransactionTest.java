package com.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentTransactionTest {
    
    public static void main(String[] args) {
        try {
            EMPDAO empDAO = new EMPDAO();
            
            // Initialize data
            System.out.println("Initializing test data...");
            initializeTestData(empDAO);
            
            // Display initial state
            System.out.println("Initial database state:");
            displayDatabaseState(empDAO);
            
            // Display SQLite transaction control information
            printSQLiteTransactionInfo();
            
            // Prepare concurrent clients
            int clientCount = 5; // Set number of concurrent clients
            CountDownLatch latch = new CountDownLatch(1);
            ExecutorService executor = Executors.newFixedThreadPool(clientCount);
            
            // Target employee ID - all clients will operate on this record simultaneously to create conflicts
            String targetEmployeeId = "E1";
            System.out.println("\n=== Conflict Test: All clients will operate on the same target record " + targetEmployeeId + " ===");
            
            System.out.println("\nStarting " + clientCount + " concurrent clients...");
            
            // Create and submit client tasks
            for (int i = 1; i <= clientCount; i++) {
                executor.submit(new TransactionClient("" + i, empDAO, latch, targetEmployeeId));
            }
            
            // Short delay to ensure all clients are ready
            Thread.sleep(1000);
            
            // Release the latch, allowing all clients to start executing simultaneously
            System.out.println("\n==== All clients begin executing concurrent transaction operations ====");
            latch.countDown();
            
            // Shut down the executor and wait for all tasks to complete
            executor.shutdown();
            boolean finished = executor.awaitTermination(30, TimeUnit.SECONDS);
            
            if (finished) {
                System.out.println("\nAll client tasks have completed");
            } else {
                System.out.println("\nWait timeout, some client tasks may not have completed");
                executor.shutdownNow();
            }
            
            // Display final state
            System.out.println("\n===== Database state after concurrent transaction execution: =====");
            displayDatabaseState(empDAO);
            
            // Summary
            summarizeTest(targetEmployeeId);
            
        } catch (Exception e) {
            System.out.println("Error occurred during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void initializeTestData(EMPDAO empDAO) throws SQLException {
        // Clear existing data
        empDAO.clearAllEmployees();
        
        // Add some test data
        empDAO.addNewEmployee("E1", "Employee1", "Manager");
        empDAO.addNewEmployee("E2", "Employee2", "Engineer");
        empDAO.addNewEmployee("E3", "Employee3", "Sales");
    }
    
    private static void displayDatabaseState(EMPDAO empDAO) throws SQLException {
        List<EMP> employees = empDAO.getAllEmployees();
        System.out.println("Total employees: " + employees.size());
        System.out.println("Employee details:");
        for (EMP emp : employees) {
            System.out.println("  " + emp);
        }
        System.out.println();
    }
    
    private static void printSQLiteTransactionInfo() {
        System.out.println("\n===== SQLite Transaction Control Information =====");
        System.out.println("1. Default isolation level: SERIALIZABLE (highest level, fully isolated)");
        System.out.println("2. Locking mechanism: when one connection is writing, other connections cannot write simultaneously");
        System.out.println("3. Conflict handling: when lock acquisition fails, SQLite typically throws a 'database is locked' error");
        System.out.println("4. Timeout: by default, SQLite waits for some time before timing out when encountering a lock");
        System.out.println("5. Deadlock: SQLite avoids deadlocks by allowing only one write operation at a time");
        System.out.println("=================================\n");
    }
    
    private static void summarizeTest(String targetEmployeeId) {
        System.out.println("\n===== Test Summary =====");
        System.out.println("1. All clients attempted to operate on the same record (ID: " + targetEmployeeId + ")");
        System.out.println("2. SQLite uses a file-lock based concurrency control mechanism");
        System.out.println("3. When multiple clients try to modify the same data, SQLite will:");
        System.out.println("   - Allow the first transaction that acquires the write lock to commit successfully");
        System.out.println("   - Block other transactions from accessing, resulting in 'database is locked' errors");
        System.out.println("   - Rejected transactions need to be rolled back, and the application can choose to retry later");
        System.out.println("4. This mechanism ensures data consistency, but may cause some operations to be rejected");
        System.out.println("====================");
    }
} 