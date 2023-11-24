package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.repository.AircraftRepository;
import com.prigozhaeva.aerocalculations.repository.AirlineRepository;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AircraftServiceImpl implements AircraftService {
    private AircraftRepository aircraftRepository;
    private AirlineRepository airlineRepository;

    public AircraftServiceImpl(AircraftRepository aircraftRepository, AirlineRepository airlineRepository) {
        this.aircraftRepository = aircraftRepository;
        this.airlineRepository = airlineRepository;
    }

    @Override
    public void importAircrafts(String path) {
        List<Aircraft> aircrafts = parse(path);
        if (aircrafts != null && aircrafts.size() != 0) {
            aircraftRepository.saveAll(aircrafts);
        }
    }

    @Override
    public List<Aircraft> findAircraftsByTailNumber(String tailNumber) {
        return aircraftRepository.findAircraftsByTailNumberContains(tailNumber);
    }

    @Override
    public Aircraft findAircraftByTailNumber(String tailNumber) {
        return aircraftRepository.findAircraftByTailNumber(tailNumber);
    }

    @Override
    public Aircraft createOrUpdateAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    @Override
    public List<Aircraft> fetchAll() {
        return aircraftRepository.findAll();
    }


    public List<Aircraft> parse(String path) {
        List<Aircraft> aircrafts;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            aircrafts = new ArrayList<>();
            Aircraft aircraft = new Aircraft();
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    switch (reader.getName().getLocalPart()) {
                        case "aircraft":
                            aircraft = new Aircraft();
                            break;
                        case "airlineId":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                Long id = Long.valueOf(reader.getText());
                                Airline airline = airlineRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException("Airline with id " + id + " Not Found"));
                                aircraft.setAirline(airline);
                            }
                        case "aircraftType":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                aircraft.setAircraftType(reader.getText());
                            }
                            break;
                        case "passengerCapacity":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                aircraft.setPassengerCapacity(Integer.parseInt(reader.getText()));
                            }
                            break;
                        case "tailNumber":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                aircraft.setTailNumber(reader.getText());
                            }
                            break;
                        case "MTOW":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                aircraft.setMTOW(Integer.parseInt(reader.getText()));
                            }
                            break;
                    }
                }
                if (reader.getEventType() == XMLEvent.END_ELEMENT) {
                    aircrafts.add(aircraft);
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return aircrafts;
    }

}
