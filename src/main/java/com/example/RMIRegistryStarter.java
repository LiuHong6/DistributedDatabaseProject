package com.example;

import java.rmi.registry.LocateRegistry;

public class RMIRegistryStarter {
    public static void main(String[] args) {
        try {
            // Start RMI Registry at default port (1099)
            LocateRegistry.createRegistry(1099);
            System.out.println("RMI Registry started on port 1099...");
            
            // Keeps the registry running
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            System.err.println("RMI Registry error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
