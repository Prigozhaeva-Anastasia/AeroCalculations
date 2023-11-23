package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.parser.STAXParser;
import com.prigozhaeva.aerocalculations.repository.AirlineRepository;
import com.prigozhaeva.aerocalculations.service.AirlineService;
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
public class AirlineServiceImpl implements AirlineService {
    private AirlineRepository airlineRepository;

    public AirlineServiceImpl(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public void importAirlines(String path) {
        List<Airline> airlines = parse(path);
        if (airlines != null && airlines.size() != 0) {
            airlineRepository.saveAll(airlines);
        }
    }

    @Override
    public List<Airline> findAirlinesByAirlineName(String name) {
        List<Airline> airlines = airlineRepository.findAirlinesByNameContainsIgnoreCase(name);
        if (airlines.size() != 0) return  airlines;
        else return airlineRepository.findAirlinesByPayerNameContainsIgnoreCase(name);
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

    @Override
    public Airline createOrUpdateAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    @Override
    public List<Airline> fetchAll() {
        return airlineRepository.findAll();
    }

    public static List<Airline> parse(String path) {
        List<Airline> airlines;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            airlines = new ArrayList<>();
            Airline airline = new Airline();
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    switch (reader.getName().getLocalPart()) {
                        case "corp":
                            airline = new Airline();
                            break;
                        case "id":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setId(Long.valueOf(reader.getText()));
                            }
                        case "name":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setPayerName(reader.getText());
                            }
                            break;
                        case "name_short":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setName(reader.getText());
                            }
                            break;
                        case "country_code":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setCountryCode(reader.getText());
                            }
                            break;
                        case "TEL":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setPhone_number(reader.getText());
                            }
                            break;
                        case "ADR":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                airline.setAddress(reader.getText());
                            }
                            break;
                    }
                }
                if (reader.getEventType() == XMLEvent.END_ELEMENT) {
                    Airline fAirline = airline;
                    boolean elementExists = airlines.stream()
                            .anyMatch(obj -> obj.getId().equals(fAirline.getId()));
                    if (!elementExists) {
                        airlines.add(fAirline);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return airlines;
    }
}
