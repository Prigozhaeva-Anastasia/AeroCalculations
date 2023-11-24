package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.Aircraft;

import java.util.List;

public interface AircraftService {
    List<Aircraft> fetchAll();
    void importAircrafts(String path);
    Aircraft findAircraftByTailNumber(String tailNumber);
    Aircraft createOrUpdateAircraft(Aircraft aircraft);
}
