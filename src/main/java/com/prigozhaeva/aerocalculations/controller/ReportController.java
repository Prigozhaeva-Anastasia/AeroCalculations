package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import com.prigozhaeva.aerocalculations.service.FlightService;
import com.prigozhaeva.aerocalculations.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
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
    private ReportService reportService;

    public ReportController(FlightService flightService, AirlineService airlineService, ReportService reportService) {
        this.flightService = flightService;
        this.airlineService = airlineService;
        this.reportService = reportService;
    }

    @GetMapping(value = "/flightDynamics")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String flightDynamics(Model model, int year, int month) {
        Map<Integer, Long> averageFlightsPerHour = reportService.flightDynamics(year, month);
        model.addAttribute(AVERAGE_FLIGHTS_PER_HOUR, averageFlightsPerHour);
        Long maxAverageFlights = findMaxValue(averageFlightsPerHour);
        if (averageFlightsPerHour.size() != 0) {
            model.addAttribute(MAX_AVERAGE_FLIGHTS, maxAverageFlights.intValue() + 1);
        }
        return "report-views/flightDynamics";
    }

    @GetMapping(value = "/airportInfrastructureDynamics")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String airportInfrastructureDynamics(Model model, String date) {
        Map<String, Long> countMap = reportService.airportInfrastructureDynamics(date);
        model.addAttribute(DATE, LocalDate.parse(date).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        if (countMap.size() != 0) {
            Long maxTimesUsing = findMaxValueOfInfrastructure(countMap);
            model.addAttribute(MAX_COUNT_OF_INFRASTRUCTURE, maxTimesUsing.intValue() + 1);
        }
        model.addAttribute(COUNTS_OF_EVERY_INFRASTRUCTURE, countMap);
        return "report-views/airportInfrastructureDynamics";
    }

    @GetMapping(value = "/changesInNumOfAirlinesDynamics")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
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

    @GetMapping(value = "/chartOfFeesChargedByAirlines")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String chartOfFeesChargedByAirlines(int year, Model model) {
        Map<Integer, BigDecimal> totalCostOfProvidedServicesPerMonth = reportService.chartOfFeesChargedByAirlines(year);
        BigDecimal maxAverageTotalCost = findMaxValueOfTotalCost(totalCostOfProvidedServicesPerMonth);
        if (totalCostOfProvidedServicesPerMonth.size() != 0) {
            model.addAttribute(MAX_TOTAL_COST, maxAverageTotalCost.intValue() + 1);
        }
        model.addAttribute(TOTAL_COST_OF_PROVIDED_SERVICES_PER_MONTH, totalCostOfProvidedServicesPerMonth);
        model.addAttribute(YEAR, year);
        return "report-views/chartOfFeesChargedByAirlines";
    }

    private static List<Airline> findAirlinesByDepDate(List<Airline> airlines, int year, int month) {
        return airlines.stream()
                .filter(airline -> airline.getAircrafts().stream()
                        .flatMap(aircraft -> aircraft.getFlights().stream())
                        .anyMatch(flight -> flight.getDepDate().getMonth().getValue() == month && flight.getDepDate().getYear() == year)
                )
                .collect(Collectors.toList());
    }

    private static Long findMaxValue(Map<Integer, Long> averageFlightsPerHour) {
        return averageFlightsPerHour.values().stream()
                .max(Long::compare)
                .orElse(null);
    }

    private static BigDecimal findMaxValueOfTotalCost(Map<Integer, BigDecimal> totalCostOfProvidedServicesPerMonth) {
        return totalCostOfProvidedServicesPerMonth.values().stream()
                .max(BigDecimal::compareTo)
                .orElse(null);
    }

    private static Long findMaxValueOfInfrastructure(Map<String, Long> countMap) {
        return countMap.values().stream()
                .max(Long::compare)
                .orElse(null);
    }
}
