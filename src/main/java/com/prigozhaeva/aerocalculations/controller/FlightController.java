package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import com.prigozhaeva.aerocalculations.service.FlightService;
import com.prigozhaeva.aerocalculations.util.CityCodeMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/flights")
public class FlightController {
    private FlightService flightService;
    private AircraftService aircraftService;

    public FlightController(FlightService flightService, AircraftService aircraftService) {
        this.flightService = flightService;
        this.aircraftService = aircraftService;
    }

    @GetMapping(value = "/index")
    public String flights(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<FlightDTO> flightList = new CopyOnWriteArrayList<>(flightService.findFlightsDtoByFlightNumber(keyword));
        model.addAttribute(LIST_FLIGHTS, flightList);
        model.addAttribute(KEYWORD, keyword);
        return "flight-views/flights";
    }

    @GetMapping(value = "/formUpdate")
    public String updateFlight(Model model, Long flightId) {
        List<Aircraft> aircrafts = aircraftService.fetchAll();
        Flight flight = flightService.findFlightById(flightId);
        model.addAttribute(FLIGHT, flight);
        model.addAttribute(LIST_AIRCRAFTS, aircrafts);
        model.addAttribute(CITY_CODE_MAP, CityCodeMap.getCityCodeMap());
        return "flight-views/formUpdate";
    }

    @PostMapping(value = "/update")
    public String update(@Valid Flight flight, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "flight-views/formUpdate";
        }
        flightService.createOrUpdateFlight(flight);
        return "redirect:/flights/index";
    }

    @GetMapping(value = "/sortByDate")
    public String sortByDate(Model model) {
        List<Flight> flights = flightService.fetchAll();
        Collections.sort(flights, Comparator.comparing(Flight::getDepDate));
        model.addAttribute(LIST_FLIGHTS, flights);
        return "flight-views/flights";
    }

    @PostMapping(value = "/filter")
    public String filter(Model model, String flightDirection, String flightType, String date1, String date2, String time1,
                         String time2) {
        List<Flight> flights = flightService.fetchAll().stream()
                .filter(flight ->
                        (flightDirection.isEmpty() || flight.getFlightDirection().equals(flightDirection)) &&
                        (flightType.isEmpty() || flight.getFlightType().equals(flightType)) &&
                                (date1.isEmpty() || flight.getDepDate().compareTo(LocalDate.parse(date1)) >= 0) &&
                                (date2.isEmpty() || flight.getDepDate().compareTo(LocalDate.parse(date2)) <= 0) &&
                                (time1.isEmpty() || flight.getDepTime().compareTo(LocalTime.parse(time1)) >= 0) &&
                                (time2.isEmpty() || flight.getDepTime().compareTo(LocalTime.parse(time2)) <= 0))
                .collect(Collectors.toList());
        model.addAttribute(LIST_FLIGHTS, flights);
        return "flight-views/flights";
    }

    @GetMapping(value = "/formMoreDetails")
    public String fetchMoreDetails(Model model, Long flightId) {
        FlightDTO flightDTO = flightService.findFlightDtoById(flightId);
        model.addAttribute(FLIGHT, flightDTO);
        return "flight-views/formMoreDetails";
    }
}
