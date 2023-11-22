package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Airline;

import java.util.List;

public interface AirlineService {
    void importAirlines(String path);
    List<Airline> findAirlinesByAirlineName(String name);
    Airline findAirlineById(Long id);
    Airline findAirlineByAirlineName(String name);
    Airline createOrUpdateAirline(Airline airline);
}
