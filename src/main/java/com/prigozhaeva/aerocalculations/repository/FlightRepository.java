package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findFlightsByFlightNumberContainsIgnoreCase(String flightNumber);
    Flight findFlightByFlightNumberAndDepDate(String flightNumber, LocalDate date);
    List<Flight> findFlightsByDepDate(LocalDate localDate);
}
