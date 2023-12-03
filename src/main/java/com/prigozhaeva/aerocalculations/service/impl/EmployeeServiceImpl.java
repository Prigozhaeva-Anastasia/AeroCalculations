package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.repository.EmployeeRepository;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findEmployeesByFioOrEmail(String searchStr) {
        if (searchStr.isEmpty()) {
            return employeeRepository.findAll();
        } else {
            if (searchStr.contains(" ")) {
                String[] name = searchStr.split(" ");
                if (name.length == 3) {
                    return employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase(name[0], name[1], name[2]);
                } else if (name.length == 2) {
                    return employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase(name[0], name[1]);
                } else {
                    List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase(name[0], name[0], name[0]);
                    return employees != null ? employees : (List<Employee>) employeeRepository.findEmployeeByEmail(name[0]);
                }
            }
            return null;
        }
    }
}
