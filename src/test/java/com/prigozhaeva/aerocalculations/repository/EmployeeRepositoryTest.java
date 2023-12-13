package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"file:src/test/resources/db/clear_all.sql","file:src/test/resources/db/insert_script.sql"})
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Test
    void testFindEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase() {
        String LAST_NAME = "Бондаренко";
        String NAME = "Маргарита";
        String PATRONYMIC = "Александровна";
        List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase(LAST_NAME, NAME, PATRONYMIC);
        int expectedValue = 1;
        assertEquals(expectedValue, employees.size());
    }
    @Test
    void testFindEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase() {
        String LAST_NAME = "Бондаренко";
        String NAME = "Маргарита";
        List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase(LAST_NAME, NAME);
        int expectedValue = 1;
        assertEquals(expectedValue, employees.size());
    }
    @Test
    void testFindEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase() {
        String LAST_NAME2 = "Астапович";
        String NAME = "Маргарита";
        String PATRONYMIC = "Александровна";
        List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase(LAST_NAME2, NAME, PATRONYMIC);
        int expectedValue = 2;
        assertEquals(expectedValue, employees.size());
    }
    @Test
    void testFindEmployeeByEmail() {
        String EMAIL = "bondarenko@gmail.com";
        Employee employee = employeeRepository.findEmployeeByEmail(EMAIL);
        assertNotNull(employee);
    }
}
