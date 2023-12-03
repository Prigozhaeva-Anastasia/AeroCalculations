package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findEmployeesByFioOrEmail(String searchStr);
}
