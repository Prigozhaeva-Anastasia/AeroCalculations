package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import com.prigozhaeva.aerocalculations.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/reports")
public class ReportController {
    private FlightService flightService;
    private AirlineService airlineService;
    private static final Map<Integer, String> inftrastructureTypeMap = new HashMap<>();

    public ReportController(FlightService flightService, AirlineService airlineService) {
        this.flightService = flightService;
        this.airlineService = airlineService;
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

    @GetMapping(value = "/flightDynamics")
    public String flightDynamics(Model model, int year, int month) {
        List<Flight> flights = flightService.fetchAll();
        Map<LocalDate, Map<Integer, Long>> flightDynamics = analyzeFlightDynamics(flights, year, month);
        Map<Integer, Double> averageFlightsPerHour = calculateAverageFlightsPerHour(flightDynamics);
        model.addAttribute(AVERAGE_FLIGHTS_PER_HOUR, averageFlightsPerHour);
        Double maxAverageFlights = findMaxValue(averageFlightsPerHour);
        if (averageFlightsPerHour.size() != 0) {
            model.addAttribute(MAX_AVERAGE_FLIGHTS, maxAverageFlights.intValue() + 1);
        }
        return "report-views/flightDynamics";
    }

    @GetMapping(value = "/airportInfrastructureDynamics")
    public String airportInfrastructureDynamics(Model model, String date) {
        List<Flight> flights = flightService.findFlightsByDepDate(LocalDate.parse(date));
        Map<String, Long> countMap = new HashMap<>();
        if (flights != null) {
            countMap = flights.stream()
                    .flatMap(flight -> flight.getProvidedServices().stream())
                    .filter(providedService -> inftrastructureTypeMap.containsKey(providedService.getService().getId().intValue()))
                    .collect(Collectors.groupingBy(
                            providedService -> inftrastructureTypeMap.get(providedService.getService().getId().intValue()),
                            Collectors.counting()
                    ));
            model.addAttribute(DATE, LocalDate.parse(date).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            if (countMap.size() != 0) {
                Long maxTimesUsing = findMaxValueOfInfrastructure(countMap);
                model.addAttribute(MAX_COUNT_OF_INFRASTRUCTURE, maxTimesUsing.intValue() + 1);
            }
        }
        model.addAttribute(COUNTS_OF_EVERY_INFRASTRUCTURE, countMap);
        return "report-views/airportInfrastructureDynamics";
    }

    @GetMapping(value = "/changesInNumOfAirlinesDynamics")
    public String changesInNumOfAirlinesDynamics(Model model, int year, int month) {
        List<Airline> airlines = airlineService.fetchAll();
        List<Airline> result = findAirlinesByDepDate(airlines, year, month);
        List<Airline> currentResult = findAirlinesByDepDate(airlines, LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        double percentOfAirlines = Double.parseDouble(new DecimalFormat("#0.00").format((double)result.size() * 100 / airlines.size()).replace(',', '.'));
        double percentOfUnusedAirlines = 100 - percentOfAirlines;
        String monthStr = Month.of(month).getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));
        String monthFormat = monthStr.substring(0, monthStr.length() - 1) + "ь";
        model.addAttribute(MONTH, monthFormat);
        model.addAttribute(YEAR, year);
        model.addAttribute(PERCENT_OF_AIRLINES, percentOfAirlines);
        model.addAttribute(PERCENT_OF_UNUSED_AIRLINES, percentOfUnusedAirlines);
        double percentOfAirlinesCur = Double.parseDouble(new DecimalFormat("#0.00").format((double)currentResult.size() * 100 / airlines.size()).replace(',', '.'));
        double percentOfUnusedAirlinesCur = 100 - percentOfAirlinesCur;
        String monthStrCur = Month.of(LocalDate.now().getMonthValue()).getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));
        String monthFormatCur = monthStrCur.substring(0, monthStrCur.length() - 1) + "ь";
        model.addAttribute(MONTH_CUR, monthFormatCur);
        model.addAttribute(YEAR_CUR, LocalDate.now().getYear());
        model.addAttribute(PERCENT_OF_AIRLINES_CUR, percentOfAirlinesCur);
        model.addAttribute(PERCENT_OF_UNUSED_AIRLINES_CUR, percentOfUnusedAirlinesCur);
        return "report-views/changesInNumOfAirlinesDynamics";
    }

    private static List<Airline> findAirlinesByDepDate(List<Airline> airlines, int year, int month) {
        return airlines.stream()
                .filter(airline -> airline.getAircrafts().stream()
                        .flatMap(aircraft -> aircraft.getFlights().stream())
                        .anyMatch(flight -> flight.getDepDate().getMonth().getValue() == month && flight.getDepDate().getYear() == year)
                )
                .collect(Collectors.toList());
    }

    private static Map<LocalDate, Map<Integer, Long>> analyzeFlightDynamics(List<Flight> flights, int year, int month) {
        return flights.stream()
                .filter(flight -> flight.getDepDate().getYear() == year && flight.getDepDate().getMonthValue() == month)
                .collect(Collectors.groupingBy(
                        Flight::getDepDate,
                        Collectors.groupingBy(
                                flight -> flight.getDepTime().getHour(),
                                Collectors.counting()
                        )
                ));
    }

    private static Map<Integer, Double> calculateAverageFlightsPerHour(Map<LocalDate, Map<Integer, Long>> flightDynamics) {
        return flightDynamics.entrySet().stream()
                .flatMap(entry -> entry.getValue().entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.averagingLong(Map.Entry::getValue)
                ));
    }

    private static Double findMaxValue(Map<Integer, Double> averageFlightsPerHour) {
        return averageFlightsPerHour.values().stream()
                .max(Double::compare)
                .orElse(null);
    }

    private static Long findMaxValueOfInfrastructure(Map<String, Long> countMap) {
        return countMap.values().stream()
                .max(Long::compare)
                .orElse(null);
    }
}
