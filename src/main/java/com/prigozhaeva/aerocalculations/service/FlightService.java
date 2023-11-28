package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Flight;

import java.util.List;

public interface FlightService {
    List<FlightDTO> findFlightsByFlightNumber(String flightNumber);
    void importFlights(String path);
    Flight findFlightByFlightNumber(String flightNumber);
    Flight createOrUpdateFlight(Flight flight);
}
