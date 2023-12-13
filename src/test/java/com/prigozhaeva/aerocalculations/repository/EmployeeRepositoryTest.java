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
    private static final String NAME = "Маргарита";
    private static final String LAST_NAME = "Бондаренко";
    private static final String LAST_NAME2 = "Астапович";
    private static final String PATRONYMIC = "Александровна";
    private static final String EMAIL = "bondarenko@gmail.com";
    @Test
    void testFindEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase() {
        List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCaseAndPatronymicContainsIgnoreCase(LAST_NAME, NAME, PATRONYMIC);
        int expectedValue = 1;
        assertEquals(expectedValue, employees.size());
    }
    @Test
    void testFindEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase() {
        List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseAndFirstNameContainsIgnoreCase(LAST_NAME, NAME);
        int expectedValue = 1;
        assertEquals(expectedValue, employees.size());
    }
    @Test
    void testFindEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase() {
        List<Employee> employees = employeeRepository.findEmployeesByLastNameContainsIgnoreCaseOrFirstNameContainsIgnoreCaseOrPatronymicContainsIgnoreCase(LAST_NAME2, NAME, PATRONYMIC);
        int expectedValue = 2;
        assertEquals(expectedValue, employees.size());
    }
    @Test
    void testFindEmployeeByEmail() {
        Employee employee = employeeRepository.findEmployeeByEmail(EMAIL);
        assertNotNull(employee);
    }
}
