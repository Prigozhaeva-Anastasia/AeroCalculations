package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.repository.FlightRepository;
import com.prigozhaeva.aerocalculations.repository.InvoiceRepository;
import com.prigozhaeva.aerocalculations.service.ReportService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.prigozhaeva.aerocalculations.constant.Constant.PAID_STATUS;


@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private FlightRepository flightRepository;
    private InvoiceRepository invoiceRepository;
    private MappingUtils mappingUtils;
    private static final Map<Integer, String> inftrastructureTypeMap = new HashMap<>();

    public ReportServiceImpl(FlightRepository flightRepository, InvoiceRepository invoiceRepository, MappingUtils mappingUtils) {
        this.flightRepository = flightRepository;
        this.invoiceRepository = invoiceRepository;
        this.mappingUtils = mappingUtils;
    }

    static {
        inftrastructureTypeMap.put(725, "взлетно-посадочные полосы");
        inftrastructureTypeMap.put(925, "взлетно-посадочные полосы");
        inftrastructureTypeMap.put(816, "стоянки");
        inftrastructureTypeMap.put(815, "стоянки");
        inftrastructureTypeMap.put(921, "стойки регистрации");
        inftrastructureTypeMap.put(948, "стойки регистрации");
        inftrastructureTypeMap.put(714, "стойки регистрации");
        inftrastructureTypeMap.put(741, "стойки регистрации");
        inftrastructureTypeMap.put(780, "пассажирские трапы");
        inftrastructureTypeMap.put(779, "пассажирские трапы");
        inftrastructureTypeMap.put(828, "пассажирские трапы");
        inftrastructureTypeMap.put(826, "пассажирские трапы");
        inftrastructureTypeMap.put(829, "пассажирские трапы");
        inftrastructureTypeMap.put(827, "пассажирские трапы");
        inftrastructureTypeMap.put(946, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(947, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(951, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(952, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(953, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(820, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(1041, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(821, "транспортные средства для перевозки пассажиров");
        inftrastructureTypeMap.put(973, "хостовая система DCS");
    }

    @Override
    public Map<Integer, Long> flightDynamics(int year, int month) {
        List<Flight> flights = flightRepository.findAll();
        Map<LocalDate, Map<Integer, Long>> flightDynamics = analyzeFlightDynamics(flights, year, month, null);
        return calculateAverageFlightsPerHour(flightDynamics);
    }

    @Override
    public Map<String, Long> airportInfrastructureDynamics(String date) {
        List<Flight> flights = flightRepository.findFlightsByDepDate(LocalDate.parse(date));
        Map<String, Long> countMap = new HashMap<>();
        if (flights != null) {
            countMap = flights.stream()
                    .flatMap(flight -> flight.getProvidedServices().stream())
                    .filter(providedService -> inftrastructureTypeMap.containsKey(providedService.getService().getId().intValue()))
                    .collect(Collectors.groupingBy(
                            providedService -> inftrastructureTypeMap.get(providedService.getService().getId().intValue()),
                            Collectors.counting()
                    ));
        }
        return countMap;
    }

    @Override
    public Map<Integer, Long> flightDynamicsOnWeekDay(int year, int month, int weekDay) {
        List<Flight> flights = flightRepository.findAll();
        Map<LocalDate, Map<Integer, Long>> flightDynamics = analyzeFlightDynamics(flights, year, month, weekDay);
        return calculateAverageFlightsPerHour(flightDynamics);
    }

    @Override
    public Map<Integer, BigDecimal> chartOfFeesChargedByAirlines(int year) {
        List<InvoiceDTO> invoiceDTOList = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getPaymentState().equals(PAID_STATUS) && invoice.getInvoiceCreationDate().getYear() == year)
                .map(mappingUtils::mapToInvoiceDTO)
                .collect(Collectors.toList());
        return IntStream.rangeClosed(1, 12)
                .boxed()
                .collect(Collectors.toMap(
                        month -> month,
                        month -> invoiceRepository.findAll().stream()
                                .filter(invoice -> invoice.getPaymentState().equals(PAID_STATUS) && invoice.getInvoiceCreationDate().getYear() == year && invoice.getInvoiceCreationDate().getMonthValue() == month)
                                .map(mappingUtils::mapToInvoiceDTO)
                                .map(InvoiceDTO::getTotalCost)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)));
    }

    private Map<LocalDate, Map<Integer, Long>> analyzeFlightDynamics(List<Flight> flights, int year, int month, Integer weekDay) {
        return flights.stream()
                .filter(flight -> flight.getDepDate().getYear() == year && flight.getDepDate().getMonthValue() == month &&
                        (weekDay == null || flight.getDepDate().getDayOfWeek().getValue() == weekDay))
                .collect(Collectors.groupingBy(
                        Flight::getDepDate,
                        Collectors.groupingBy(
                                flight -> flight.getDepTime().getHour(),
                                Collectors.counting()
                        )
                ));
    }

    private Map<Integer, Long> calculateAverageFlightsPerHour(Map<LocalDate, Map<Integer, Long>> flightDynamics) {
        return flightDynamics.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.collectingAndThen(
                                Collectors.averagingLong(Map.Entry::getValue),
                                average -> (long) Math.floor(average)
                        )
                ));
    }
}
