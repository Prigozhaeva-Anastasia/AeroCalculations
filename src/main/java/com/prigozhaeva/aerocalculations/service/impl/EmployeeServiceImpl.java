package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.User;
import com.prigozhaeva.aerocalculations.repository.EmployeeRepository;
import com.prigozhaeva.aerocalculations.repository.UserRepository;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private UserRepository userRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Employee> findEmployeesByFioOrEmail(String searchStr) {
        List<Employee> employees = new ArrayList<>();
        if (searchStr.isEmpty()) {
            employees = employeeRepository.findAll();
        } else {
            if (searchStr.contains(" ")) {
                String[] name = searchStr.split(" ");
                if (name.length == 3) {
                    employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase(name[0], name[1], name[2]);
                } else if (name.length == 2) {
                    employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase(name[0], name[1]);
                }
            } else {
                employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase(searchStr, searchStr, searchStr);
                if (employees.size() == 0) {
                    employees.add(employeeRepository.findEmployeeByEmail(searchStr));
                }
            }
        }
        return checkAreEmployeesActual(employees);
    }

    private List<Employee> checkAreEmployeesActual(List<Employee> employees) {
        return employees.stream()
                    .filter(emp->emp.getUser().getRoles().size() != 0)
                    .collect(Collectors.toList());
    }

    @Override
    public Employee createEmployee(Long userId, String lastName, String firstName, String patronymic, String phoneNumber, String position, String img) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " Not Found"));
        return employeeRepository.save(Employee.builder()
                .lastName(lastName)
                .firstName(firstName)
                .patronymic(patronymic)
                .phoneNumber(phoneNumber)
                .position(position)
                .img(img)
                .user(user)
                .build());
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " Not Found"));
        employee.getUser().setRoles(null);
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> fetchAll() {
        return employeeRepository.findAll();
    }
}
