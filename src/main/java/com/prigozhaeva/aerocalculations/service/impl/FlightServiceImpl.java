package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.repository.AircraftRepository;
import com.prigozhaeva.aerocalculations.repository.FlightRepository;
import com.prigozhaeva.aerocalculations.service.FlightService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {
    private FlightRepository flightRepository;
    private AircraftRepository aircraftRepository;
    private MappingUtils mappingUtils;

    public FlightServiceImpl(FlightRepository flightRepository, AircraftRepository aircraftRepository, MappingUtils mappingUtils) {
        this.flightRepository = flightRepository;
        this.aircraftRepository = aircraftRepository;
        this.mappingUtils = mappingUtils;
    }

    @Override
    public List<FlightDTO> findFlightsDtoByFlightNumber(String flightNumber) {
        return flightRepository.findFlightsByFlightNumberContainsIgnoreCase(flightNumber).stream()
                .map(mappingUtils::mapToFlightDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void importFlights(String path) {
        List<Flight> flights = parse(path);
        if (flights != null && flights.size() != 0) {
            flightRepository.saveAll(flights);
        }
    }

    @Override
    public Flight findFlightById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight with id " + flightId + " Not Found"));
    }

    @Override
    public FlightDTO findFlightDtoById(Long flightId) {
        return mappingUtils.mapToFlightDTO(flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight with id " + flightId + " Not Found")));
    }

    @Override
    public Flight createOrUpdateFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public List<Flight> fetchAll() {
        return flightRepository.findAll();
    }

    @Override
    public Flight findFlightByFlightNumberAndDepDate(String flightNumber, LocalDate date) {
        return flightRepository.findFlightByFlightNumberAndDepDate(flightNumber, date);
    }

    @Override
    public List<Flight> findFlightsByDepDate(LocalDate date) {
        return flightRepository.findFlightsByDepDate(date);
    }


    public List<Flight> parse(String path) {
        List<Flight> flights;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            flights = new ArrayList<>();
            Flight flight = new Flight();
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    switch (reader.getName().getLocalPart()) {
                        case "flight":
                            flight = new Flight();
                            break;
                        case "flight_id":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setId(Long.valueOf(reader.getText()));
                            }
                        case "nr":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setFlightNumber(reader.getText());
                            }
                        case "pv_kobra":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setFlightDirection(reader.getText());
                            }
                            break;
                        case "type_regul":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setFlightType(reader.getText());
                            }
                            break;
                        case "route":
                            reader.next();
                            reader.next();
                            break;
                        case "ap_dep_iata":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setDepCity(reader.getText());
                            }
                            break;
                        case "dt_act_dep":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                String date_time = reader.getText();
                                flight.setDepDate(LocalDate.parse(date_time.split("T")[0]));
                                flight.setDepTime(LocalTime.parse(date_time.split("T")[1]));
                            }
                            break;
                        case "ap_arr_iata":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setArrCity(reader.getText());
                            }
                            break;
                        case "dt_act_arr":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                String date_time = reader.getText();
                                flight.setArrDate(LocalDate.parse(date_time.split("T")[0]));
                                flight.setArrTime(LocalTime.parse(date_time.split("T")[1]));
                            }
                            break;
                        case "bort":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                Aircraft aircraft = aircraftRepository.findAircraftByTailNumberIgnoreCase(reader.getText());
                                flight.setAircraft(aircraft);
                            }
                            break;
                        case "bag_sum":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setLuggageWeight(Integer.parseInt(reader.getText()));
                            }
                            break;
                        case "pax_vzr":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setNumOfAdults(Integer.parseInt(reader.getText()));
                            }
                            break;
                        case "pax_rb":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setNumOfChildren(Integer.parseInt(reader.getText()));
                            }
                            break;
                        case "pax_rm":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                flight.setNumOfBabies(Integer.parseInt(reader.getText()));
                            }
                            break;
                    }
                }
                if (reader.getEventType() == XMLEvent.END_ELEMENT) {
                    flights.add(flight);
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return flights;
    }
}
