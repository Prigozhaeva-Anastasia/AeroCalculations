package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.repository.FlightRepository;
import com.prigozhaeva.aerocalculations.repository.ProvideServiceRepository;
import com.prigozhaeva.aerocalculations.repository.ServiceRepository;
import com.prigozhaeva.aerocalculations.service.ProvidedServiceService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProvidedServiceServiceImpl implements ProvidedServiceService {
    private ProvideServiceRepository provideServiceRepository;
    private ServiceRepository serviceRepository;
    private FlightRepository flightRepository;

    public ProvidedServiceServiceImpl(ProvideServiceRepository provideServiceRepository, ServiceRepository serviceRepository, FlightRepository flightRepository) {
        this.provideServiceRepository = provideServiceRepository;
        this.serviceRepository = serviceRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public List<ProvidedService> findProvidedServicesByFlightNumber(String flightNumber) {
        return provideServiceRepository.findProvidedServicesByFlightNumber(flightNumber);
    }

    @Override
    public void importProvidedServices(String path) {
        List<ProvidedService> providedServices = parse(path);
        if (providedServices != null && providedServices.size() != 0) {
            provideServiceRepository.saveAll(providedServices);
        }
    }

    @Override
    public ProvidedService createProvidedService(Long serviceId, String flightNumber) {
        com.prigozhaeva.aerocalculations.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service with id " + serviceId + " Not Found"));
        Flight flight = flightRepository.findFlightByFlightNumberIgnoreCase(flightNumber);
        return ProvidedService.builder()
                .service(service)
                .flight(flight)
                .build();
    }

    @Override
    public ProvidedService findProvidedServiceById(Long id) {
        return provideServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provided service with id " + id + " Not Found"));
    }

    @Override
    public ProvidedService createOrUpdateProvidedService(Long providedServiceId, short amount) {
        ProvidedService providedService = findProvidedServiceById(providedServiceId);
        providedService.setAmount(amount);
        return provideServiceRepository.save(providedService);
    }


    public List<ProvidedService> parse(String path) {
        List<ProvidedService> providedServices;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            providedServices = new ArrayList<>();
            ProvidedService providedService;
            Long serviceId = null;
            String flightNumber = null;
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if ("row".equals(reader.getLocalName())) {
                            serviceId = Long.valueOf(reader.getAttributeValue(null, "CLAUSE_ID"));
                            flightNumber = reader.getAttributeValue(null, "econ_flight_name");
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if ("row".equals(reader.getLocalName())) {
                            providedService = createProvidedService(serviceId, flightNumber);
                            providedServices.add(providedService);
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return providedServices;
    }
}
