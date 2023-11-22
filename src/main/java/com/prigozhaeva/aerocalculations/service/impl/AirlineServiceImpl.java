package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.parser.STAXParser;
import com.prigozhaeva.aerocalculations.repository.AirlineRepository;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AirlineServiceImpl implements AirlineService {
    private AirlineRepository airlineRepository;

    public AirlineServiceImpl(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public void importAirlines(String path) {
        List<Airline> airlines = STAXParser.parse(path);
        if (airlines != null && airlines.size() != 0) {
            airlineRepository.saveAll(airlines);
        }
    }

    @Override
    public List<Airline> findAirlinesByAirlineName(String name) {
        return airlineRepository.findAirlinesByNameContains(name);
    }
}
