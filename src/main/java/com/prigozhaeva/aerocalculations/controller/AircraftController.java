package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/aircrafts")
public class AircraftController {
    private AircraftService aircraftService;
    private AirlineService airlineService;

    public AircraftController(AircraftService aircraftService, AirlineService airlineService) {
        this.aircraftService = aircraftService;
        this.airlineService = airlineService;
    }

    @GetMapping(value = "/index")
    public String aircrafts(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Aircraft> aircraftList = new CopyOnWriteArrayList<>(aircraftService.findAircraftsByTailNumber(keyword));
        List<Airline> airlines = airlineService.fetchAll();
        model.addAttribute(LIST_AIRCRAFTS, aircraftList);
        model.addAttribute(KEYWORD, keyword);
        model.addAttribute(LIST_AIRLINES, airlines);
        return "aircraft-views/aircrafts";
    }

    @GetMapping(value = "/formUpdate")
    public String updateAircraft(Model model, String tailNumber) {
        List<Airline> airlines = airlineService.fetchAll();
        Aircraft aircraft = aircraftService.findAircraftByTailNumber(tailNumber);
        model.addAttribute(AIRCRAFT, aircraft);
        model.addAttribute(LIST_AIRLINES, airlines);
        return "aircraft-views/formUpdate";
    }

    @PostMapping(value = "/update")
    public String update(@Valid Aircraft aircraft, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "aircraft-views/formUpdate";
        }
        aircraftService.createOrUpdateAircraft(aircraft);
        return "redirect:/aircrafts/index";
    }

    @GetMapping(value = "/sortByPassengerCapacity")
    public String sortByPassengerCapacity(Model model) {
        List<Aircraft> aicrafts = aircraftService.fetchAll();
        Collections.sort(aicrafts, Comparator.comparing(Aircraft::getPassengerCapacity));
        model.addAttribute(LIST_AIRCRAFTS, aicrafts);
        return "aircraft-views/aircrafts";
    }

    @GetMapping(value = "/sortByMTOW")
    public String sortByMTOW(Model model) {
        List<Aircraft> aicrafts = aircraftService.fetchAll();
        Collections.sort(aicrafts, Comparator.comparing(Aircraft::getMTOW));
        model.addAttribute(LIST_AIRCRAFTS, aicrafts);
        return "aircraft-views/aircrafts";
    }

    @PostMapping(value = "/filterByAirline")
    public String filterByAirline(Model model, String airlineId) {
        List<Aircraft> aircrafts;
        aircrafts = aircraftService.fetchAll().stream()
                .filter(element->element.getAirline().getId().equals(Long.parseLong(airlineId)))
                .collect(Collectors.toList());
        model.addAttribute(LIST_AIRCRAFTS, aircrafts);
        return "aircraft-views/aircrafts";
    }
}
