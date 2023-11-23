package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    List<Airline> findAirlinesByNameContainsIgnoreCase(String name);
    List<Airline> findAirlinesByPayerNameContainsIgnoreCase(String name);
    Airline findAirlineByName(String name);
}
