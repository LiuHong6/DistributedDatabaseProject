package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EmpServer {
    public static void main(String[] args) {
        try {
            EmpRemote empRemote = new EmpRemoteImpl();

            Registry registry;
            try {
                // Try to create a new registry if one isn't already running
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("RMI Registry created.");
            } catch (Exception e) {
                // If registry already exists, get it instead
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("RMI Registry found.");
            }

            registry.rebind("EmpService", empRemote);
            System.out.println("EMP RMI Server running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}