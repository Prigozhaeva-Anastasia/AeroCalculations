package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase(String lastName, String firstName, String patronymic);
    List<Employee> findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase(String lastName, String firstName);
    List<Employee> findEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase(String lastName, String firstName, String patronymic);
    @Query(value = "select e from Employee as e where e.user.email=:email")
    Employee findEmployeeByEmail(@Param("email") String email);
}
