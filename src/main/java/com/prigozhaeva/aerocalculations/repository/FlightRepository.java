package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findFlightsByFlightNumberContainsIgnoreCase(String flightNumber);
    Flight findFlightByFlightNumberIgnoreCase(String flightNumber);
}
