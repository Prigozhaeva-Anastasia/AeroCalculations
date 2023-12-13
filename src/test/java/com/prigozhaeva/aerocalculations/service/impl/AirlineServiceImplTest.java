package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.repository.AirlineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {
    @Mock
    private AirlineRepository airlineRepository;
    @InjectMocks
    private AirlineServiceImpl airlineService;
    @Test
    void testImportAirlines() {
        airlineService.importAirlines("D:/diploma/import/Aircompany.xml");

        verify(airlineRepository, times(1)).saveAll(any());
    }

    @Test
    void testFindAirlinesByAirlineName() {
        String airlineName = "Aeroflot";
        airlineService.findAirlinesByAirlineName(airlineName);
        verify(airlineRepository).findAirlinesByNameContainsIgnoreCase(airlineName);
    }

    @Test
    void testFindAirlineById() {
        Long id = 1L;
        Airline airline =  Airline.builder()
                .id(1L)
                .build();

        when(airlineRepository.findById(id)).thenReturn(Optional.of(airline));

        Airline result = airlineService.findAirlineById(id);

        assertEquals(airline, result);
    }

    @Test
    void testFindAirlineByAirlineName() {
        String name = "Aeroflot";
        Airline airline = new Airline();

        when(airlineRepository.findAirlineByName(name)).thenReturn(airline);

        Airline result = airlineService.findAirlineByAirlineName(name);

        assertEquals(airline, result);
    }

    @Test
    void createOrUpdateAirline() {
        Airline airline = new Airline();

        when(airlineRepository.save(airline)).thenReturn(airline);

        Airline result = airlineService.createOrUpdateAirline(airline);

        assertEquals(airline, result);
    }

    @Test
    void testFetchAll() {
        List<Airline> airlines = Arrays.asList(new Airline());

        when(airlineRepository.findAll()).thenReturn(airlines);

        List<Airline> result = airlineService.fetchAll();

        assertEquals(airlines, result);
    }
}