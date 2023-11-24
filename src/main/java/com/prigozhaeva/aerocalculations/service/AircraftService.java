package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Airline;

import java.util.List;

public interface AircraftService {
    void importAircrafts(String path);
    List<Aircraft> findAircraftsByTailNumber(String tailNumber);
    Aircraft findAircraftByTailNumber(String tailNumber);
    Aircraft createOrUpdateAircraft(Aircraft aircraft);
    List<Aircraft> fetchAll();
}
