package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findEmployeesByFioOrEmail(String searchStr);
    Employee findEmployeeByEmail(String email);
    Employee createEmployee(Long userId, String lastName, String firstName, String patronymic, String phoneNumber, String position, String img);
    void deleteEmployee(Long employeeId);
    List<Employee> fetchAll();
}
