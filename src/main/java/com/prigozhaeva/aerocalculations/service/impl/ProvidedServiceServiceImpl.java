package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.entity.CurrencyRate;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.entity.RushHour;
import com.prigozhaeva.aerocalculations.repository.FlightRepository;
import com.prigozhaeva.aerocalculations.repository.ProvideServiceRepository;
import com.prigozhaeva.aerocalculations.repository.RushHourRepository;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.prigozhaeva.aerocalculations.constant.Constant.BYN;
import static com.prigozhaeva.aerocalculations.constant.Constant.CHILDREN;

@Service
@Transactional
public class ProvidedServiceServiceImpl implements ProvidedServiceService {
    private ProvideServiceRepository provideServiceRepository;
    private ServiceRepository serviceRepository;
    private FlightRepository flightRepository;
    private CurrencyRateService currencyRateService;
    private RushHourRepository rushHourRepository;

    public ProvidedServiceServiceImpl(ProvideServiceRepository provideServiceRepository, ServiceRepository serviceRepository, FlightRepository flightRepository, CurrencyRateService currencyRateService, RushHourRepository rushHourRepository) {
        this.provideServiceRepository = provideServiceRepository;
        this.serviceRepository = serviceRepository;
        this.flightRepository = flightRepository;
        this.currencyRateService = currencyRateService;
        this.rushHourRepository = rushHourRepository;
    }

    @Override
    public List<ProvidedService> findProvidedServicesByFlightId(Long flightId) {
        return provideServiceRepository.findProvidedServicesByFlightId(flightId);
    }

    @Override
    public void importProvidedServices(String path) {
        List<ProvidedService> providedServices = parse(path);
        if (providedServices != null && providedServices.size() != 0) {
            provideServiceRepository.saveAll(providedServices);
        }
    }

    @Override
    public ProvidedService createProvidedService(Long serviceId, Long flightId) {
        com.prigozhaeva.aerocalculations.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException("Service with id " + serviceId + " Not Found"));
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight with id " + flightId + " Not Found"));
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

    @Override
    public List<ProvidedService> updateProvidedServices(List<ProvidedService> providedServices, Long flightId) {
        List<ProvidedService> providedServiceList = new ArrayList<>();
        for (ProvidedService providedService : providedServices) {
            if (providedService.getId() != null) {
                ProvidedService providedServiceDB = provideServiceRepository.findById(providedService.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Provided service with id " + providedService.getId() + " Not Found"));
                providedServiceDB.setAmount(providedService.getAmount());
                providedServiceList.add(provideServiceRepository.save(providedServiceDB));
            } else {
                providedService.setService(serviceRepository.findById(providedService.getService().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Service with id " + providedService.getService().getId() + " Not Found")));
                providedService.setFlight(flightRepository.findById(flightId)
                        .orElseThrow(() -> new EntityNotFoundException("Flight with id " + flightId + " Not Found")));
                providedServiceList.add(provideServiceRepository.save(providedService));
            }
        }
        return providedServiceList;
    }

    @Override
    public void countValueOfProvidedService(ProvidedService providedService, String currency, LocalDate creationDate) {
        CurrencyRate currencyRate = currencyRateService.getCurrencyRate(currency, creationDate);
        BigDecimal bynRate = currencyRateService.getCurrencyRate(BYN, creationDate).getValue();
        BigDecimal providedServiceValue = BigDecimal.ZERO;
        if (providedService.getService().getTariff() != null) {
            providedServiceValue = providedService.getService().getTariff()
                    .multiply(BigDecimal.valueOf(providedService.getAmount()))
                    .multiply(bynRate)
                    .divide(currencyRate.getValue(), 2, RoundingMode.HALF_UP);
        }
        if (providedService.getService().getName().contains(CHILDREN)) {
            providedServiceValue = providedServiceValue.multiply(BigDecimal.valueOf(0.50));
        }
        if (checkingIfTheFlightIsNight(providedService.getFlight())) {
            providedServiceValue = providedServiceValue.multiply(BigDecimal.valueOf(1.05));
        }
        if (checkingIfTheFlightIsInWinterPeriod(providedService.getFlight())) {
            providedServiceValue = providedServiceValue.multiply(BigDecimal.valueOf(1.10));
        }
        if (checkingIfTheFlightIsInRushHours(providedService.getFlight())) {
            providedServiceValue = providedServiceValue.multiply(BigDecimal.valueOf(1.05));
        }
        providedService.setValue(providedServiceValue);
        provideServiceRepository.save(providedService);
    }

    private boolean checkingIfTheFlightIsNight(Flight flight) {
        return !flight.getDepTime().isBefore(LocalTime.of(22, 0)) && !flight.getDepTime().isAfter(LocalTime.of(6, 0));
    }

    private boolean checkingIfTheFlightIsInWinterPeriod(Flight flight) {
        return flight.getDepDate().getMonthValue() == 12 || flight.getDepDate().getMonthValue() == 1 || flight.getDepDate().getMonthValue() == 2;
    }

    private boolean checkingIfTheFlightIsInRushHours(Flight flight) {
        List<RushHour> rushHours = rushHourRepository.findRushHoursByWeekDay(flight.getDepDate().getDayOfWeek().getValue());
        return rushHours.stream()
                .anyMatch(rushHour -> flight.getDepTime().isAfter(rushHour.getFromTime()) && flight.getDepTime().isBefore(rushHour.getToTime()));
    }


    public List<ProvidedService> parse(String path) {
        List<ProvidedService> providedServices;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            providedServices = new ArrayList<>();
            ProvidedService providedService;
            Long serviceId = null;
            Long flightId = null;
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if ("row".equals(reader.getLocalName())) {
                            serviceId = Long.valueOf(reader.getAttributeValue(null, "CLAUSE_ID"));
                            flightId = Long.valueOf(reader.getAttributeValue(null, "ID_SPP"));
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if ("row".equals(reader.getLocalName())) {
                            providedService = createProvidedService(serviceId, flightId);
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
