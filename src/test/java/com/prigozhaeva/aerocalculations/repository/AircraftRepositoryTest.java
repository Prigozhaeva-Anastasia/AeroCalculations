package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
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
public class AircraftRepositoryTest {
    @Autowired
    private AircraftRepository aircraftRepository;
    @Test
    void testFindAircraftByTailNumberIgnoreCase() {
        String TAIL_NUMBER = "EW455PA";
        Aircraft aircraft = aircraftRepository.findAircraftByTailNumberIgnoreCase(TAIL_NUMBER);
        assertNotNull(aircraft);
    }

    @Test
    void testFindAircraftsByTailNumberContainsIgnoreCase() {
        String TAIL_NUMBER = "EW455PA";
        List<Aircraft> aircrafts = aircraftRepository.findAircraftsByTailNumberContainsIgnoreCase(TAIL_NUMBER);
        int expectedValue = 1;
        assertEquals(expectedValue, aircrafts.size());
    }
}
