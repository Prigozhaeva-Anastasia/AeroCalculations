package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"file:src/test/resources/db/clear_all.sql","file:src/test/resources/db/insert_script.sql"})
class ServiceRepositoryTest {
    @Autowired
    private ServiceRepository serviceRepository;
    private static final String SERVICE_NAME = "Пользование CIP-залом";

    @Test
    void findServicesByNameContainsIgnoreCase() {
        List<Service> services = serviceRepository.findServicesByNameContainsIgnoreCase(SERVICE_NAME);
        int expectedValue = 8;
        assertEquals(expectedValue, services.size());
    }
}