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
        Employee manager = null; // root employee
        // Employee employee = (Employee) Employee.all();
        // and use this data structure to maintain reference information needed to build the tree structure
        Map<Long, List<Employee>> employeeMap = new HashMap<>();
        List<Employee> allEmployees = Employee.all();
        for (Employee currentEmployee : allEmployees) {
            Long reportsTo = currentEmployee.getReportsTo();
            //for all employees we will get the reportsto value
                    //and then check
            if (reportsTo == 0){
                manager = currentEmployee;
            }

            List<Employee> subordinates = new LinkedList<>();
            for (Employee person : allEmployees) {
                if (person.getReportsTo() == currentEmployee.getEmployeeId()) {
                    subordinates.add(person);
                }
            }
            employeeMap.put(currentEmployee.getEmployeeId(), subordinates);
        }
        return "<ul>" + makeTree(manager, employeeMap) + "</ul>";
    }

    // TODO - currently this method just uses the employee.getReports() function, which
    //  issues a query.  Change that to use the employeeMap variable instead
    public static String makeTree(Employee employee, Map<Long, List<Employee>> employeeMap) {

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
