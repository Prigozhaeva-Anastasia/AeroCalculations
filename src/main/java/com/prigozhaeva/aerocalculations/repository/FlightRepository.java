package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findFlightsByFlightNumberContainsIgnoreCase(String flightNumber);
    Flight findFlightByFlightNumberAndDepDate(String flightNumber, LocalDate date);
    List<Flight> findFlightsByDepDate(LocalDate localDate);
    @Query(value = "select f from Flight as f where f.aircraft.airline.id=:airlineId")
    List<Flight> findFlightsByAirlineId(Long airlineId);
}
