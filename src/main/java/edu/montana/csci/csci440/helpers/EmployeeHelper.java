package edu.montana.csci.csci440.helpers;

import edu.montana.csci.csci440.model.Employee;
import edu.montana.csci.csci440.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmployeeHelper {

    public static String makeEmployeeTree() {
        // T ODO, change this to use a single query operation to get all employees
        Employee employee = Employee.find(1); // root employee
        // Employee employee = (Employee) Employee.all();
        // and use this data structure to maintain reference information needed to build the tree structure
        Map<Long, List<Employee>> employeeMap = new HashMap<>();
        List<Employee> allEmployees = Employee.all();
        for (Employee currentEmployee : allEmployees) {
            Long reportsTo = currentEmployee.getReportsTo();
            List<Employee> employees = employeeMap.get(reportsTo);
            // todo what if first employee (if else)
            if (reportsTo == null){
                employees.add(currentEmployee);
            }
            else {
                employees.add(currentEmployee);
            }
        }
        return "<ul>" + makeTree(employee, employeeMap) + "</ul>";
    }

    // TODO - currently this method just uses the employee.getReports() function, which
    //  issues a query.  Change that to use the employeeMap variable instead
    public static String makeTree(Employee employee, Map<Long, List<Employee>> employeeMap) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM employees"
             )) {
            ResultSet results = stmt.executeQuery();
            List<Employee> resultList = new LinkedList<>();
            while (results.next()) {
                //employeeMap.put();//TODO: what goes in the put?
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
        String list = "<li><a href='/employees" + employee.getEmployeeId() + "'>"
                + employee.getEmail() + "</a><ul>";
        //List<Employee> reports = employee.getReports();
        List<Employee> reports = employee.getReports();//TODO: what is this something
        for (Employee report : reports) {
            list += makeTree(report, employeeMap);
        }
        return list + "</ul></li>";
    }
}
