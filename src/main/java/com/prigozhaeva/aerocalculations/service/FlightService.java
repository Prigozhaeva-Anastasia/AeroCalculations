package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;

import java.util.List;

public interface FlightService {
    List<FlightDTO> findFlightsByFlightNumber(String flightNumber);
    void importFlights(String path);
}
