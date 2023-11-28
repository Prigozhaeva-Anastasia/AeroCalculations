package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import com.prigozhaeva.aerocalculations.service.AirlineService;
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
        List<FlightDTO> flightList = new CopyOnWriteArrayList<>(flightService.findFlightsByFlightNumber(keyword));
        model.addAttribute(LIST_FLIGHTS, flightList);
        model.addAttribute(KEYWORD, keyword);
        return "flight-views/flights";
    }

    @GetMapping(value = "/formUpdate")
    public String updateFlight(Model model, String flightNumber) {
        List<Aircraft> aircrafts = aircraftService.fetchAll();
        Flight flight = flightService.findFlightByFlightNumber(flightNumber);
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
}
