package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.parser.STAXParser;
import com.prigozhaeva.aerocalculations.repository.AirlineRepository;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    @Override
    public Airline findAirlineById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Airline with id " + id + " Not Found"));
    }

    @Override
    public Airline findAirlineByAirlineName(String name) {
        return airlineRepository.findAirlineByName(name);
    }

    public Airline createOrUpdateAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

}
