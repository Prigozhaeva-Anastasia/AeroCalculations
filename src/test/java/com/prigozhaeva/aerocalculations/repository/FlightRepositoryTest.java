package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"file:src/test/resources/db/clear_all.sql","file:src/test/resources/db/insert_script.sql"})
public class FlightRepositoryTest {
    @Autowired
    private FlightRepository flightRepository;
    private static final String FLIGHT_NUMBER = "BRU8195";
    private static final String DEP_DATE = "2023-11-04";

    @Test
    void testFindFlightsByFlightNumberContainsIgnoreCase() {
        List<Flight> flights = flightRepository.findFlightsByFlightNumberContainsIgnoreCase(FLIGHT_NUMBER);
        int expectedValue = 3;
        assertEquals(expectedValue, flights.size());
    }
    @Test
    void testFindFlightByFlightNumberAndDepDate() {
        Flight flight = flightRepository.findFlightByFlightNumberAndDepDate(FLIGHT_NUMBER, LocalDate.parse(DEP_DATE));
        assertNotNull(flight);
    }
}
