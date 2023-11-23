package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.repository.AircraftRepository;
import com.prigozhaeva.aerocalculations.repository.AirlineRepository;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AircraftServiceImpl implements AircraftService {
    private AircraftRepository aircraftRepository;

    public AircraftServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public List<Aircraft> fetchAll() {
        return aircraftRepository.findAll();
    }
}
