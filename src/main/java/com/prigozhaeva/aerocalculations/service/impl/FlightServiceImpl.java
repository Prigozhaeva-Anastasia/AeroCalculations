package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.repository.FlightRepository;
import com.prigozhaeva.aerocalculations.service.FlightService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {
    private FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Flight> findFlightsByFlightNumber(String flightNumber) {
        return flightRepository.findFlightsByFlightNumberContains(flightNumber);
    }
}
