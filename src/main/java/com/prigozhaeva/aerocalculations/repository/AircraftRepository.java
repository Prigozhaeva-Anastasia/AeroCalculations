package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {
    Aircraft findAircraftByTailNumberIgnoreCase(String tailNumber);
    List<Aircraft> findAircraftsByTailNumberContainsIgnoreCase(String tailNumber);
}
