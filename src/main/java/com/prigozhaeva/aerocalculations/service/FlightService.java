package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> findFlightsByFlightNumber(String flightNumber);
}
