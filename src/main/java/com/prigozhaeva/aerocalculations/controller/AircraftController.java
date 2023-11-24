package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import com.prigozhaeva.aerocalculations.service.AirlineService;
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
        model.addAttribute(LIST_AIRCRAFTS, aircraftList);
        model.addAttribute(KEYWORD, keyword);
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
}
