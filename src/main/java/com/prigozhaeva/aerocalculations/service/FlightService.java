package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    List<FlightDTO> findFlightsDtoByFlightNumber(String flightNumber);
    void importFlights(String path);
    Flight findFlightById(Long flightId);
    FlightDTO findFlightDtoById(Long flightId);
    Flight createOrUpdateFlight(Flight flight);
    List<Flight> fetchAll();
    Flight findFlightByFlightNumberAndDepDate(String flightNumber, LocalDate date);
}
