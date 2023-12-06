package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.AVERAGE_FLIGHTS_PER_HOUR;

@Controller
@RequestMapping(value = "/reports")
public class ReportController {
    private FlightService flightService;

    public ReportController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping(value = "/flightDynamics")
    public String flightDynamics(Model model, int year, int month) {
        List<Flight> flights = flightService.fetchAll();
        Map<LocalDate, Map<Integer, Long>> flightDynamics = analyzeFlightDynamics(flights, year, month);
        Map<Integer, Double> averageFlightsPerHour = calculateAverageFlightsPerHour(flightDynamics);
        model.addAttribute(AVERAGE_FLIGHTS_PER_HOUR, averageFlightsPerHour);
        Double maxAverageFlights = findMaxValue(averageFlightsPerHour);
        if (averageFlightsPerHour.size() != 0) {
            model.addAttribute("MAX_AVERAGE_FLIGHTS", maxAverageFlights.intValue() + 1);
        }
        return "report-views/flightDynamics";
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
}
