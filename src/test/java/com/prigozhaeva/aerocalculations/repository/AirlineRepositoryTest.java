package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Airline;
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
public class AirlineRepositoryTest {
    @Autowired
    private  AirlineRepository airlineRepository;
    @Test
    void testFindAirlinesByNameContainsIgnoreCase() {
        String AIRLINE_NAME = "ТУРКМЕНХОВАЕЛЛАРЫ";
        List<Airline> airlines = airlineRepository.findAirlinesByNameContainsIgnoreCase(AIRLINE_NAME);
        int expectedValue = 1;
        assertEquals(expectedValue, airlines.size());
    }

    @Test
    void testFindAirlinesByPayerNameContainsIgnoreCase() {
        String PAYER_NAME = "эйр";
        List<Airline> airlines = airlineRepository.findAirlinesByPayerNameContainsIgnoreCase(PAYER_NAME);
        int expectedValue = 5;
        assertEquals(expectedValue, airlines.size());
    }
    @Test
    void testFindAirlineByName() {
        String AIRLINE_NAME = "ТУРКМЕНХОВАЕЛЛАРЫ";
        Airline airline = airlineRepository.findAirlineByName(AIRLINE_NAME);
        assertNotNull(airline);
    }
}
