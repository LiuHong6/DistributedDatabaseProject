package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class EmpRemoteImpl extends UnicastRemoteObject implements EmpRemote {

    EMPDAO empDAO;

    protected EmpRemoteImpl() throws RemoteException {
        super();
        empDAO = new EMPDAO();
    }

    @Override
    public EMP findEmployeeById(String eno) throws RemoteException, SQLException {
        return empDAO.findEmployeeById(eno);
    }

    @Override
    public int addNewEmployee(String eno, String ename, String title) throws RemoteException, SQLException {
        return empDAO.addNewEmployee(eno, ename, title);
    }

    @Override
    public int updateEmployee(String eno, String ename, String title) throws RemoteException, SQLException {
        return empDAO.updateEmployee(eno, ename, title);
    }
    
    @Override
    public void clearAllEmployees() throws RemoteException, SQLException {
        empDAO.clearAllEmployees();
    }

    @Override
    public int deleteEmployee(String eno) throws RemoteException, SQLException {
        return empDAO.deleteEmployee(eno);
    }

    @Override
    public List<EMP> getAllEmployees() throws RemoteException, SQLException {
        return empDAO.getAllEmployees();
    }
    
    @Override
    public boolean batchUpdateWithTransaction(String eno1, String ename1, String title1, 
                                            String eno2, String ename2, String title2) throws RemoteException, SQLException {
        return empDAO.batchUpdateWithTransaction(eno1, ename1, title1, eno2, ename2, title2);
    }
}