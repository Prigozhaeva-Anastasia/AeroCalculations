package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;


@Controller
@RequestMapping(value = "/airlines")
public class AirlineController {
    private AirlineService airlineService;
    public AirlineController(AirlineService airlineService) {this.airlineService = airlineService;}

    @GetMapping(value = "/index")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String airlines(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Airline> airlineList = new CopyOnWriteArrayList<>(airlineService.findAirlinesByAirlineName(keyword));
        model.addAttribute(LIST_AIRLINES, airlineList);
        model.addAttribute(KEYWORD, keyword);
        return "airline-views/airlines";
    }

    @GetMapping(value = "/formUpdate")
    @PreAuthorize("hasAuthority('Admin')")
    public String updateAirline(Model model, Long airlineId) {
        Airline airline = airlineService.findAirlineById(airlineId);
        model.addAttribute(AIRLINE, airline);
        return "airline-views/formUpdate";
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('Admin')")
    public String update(@Valid Airline airline, BindingResult bindingResult) {
        if (!airline.getName().equals(airlineService.findAirlineById(airline.getId()).getName())) {
            Airline airlineDB = airlineService.findAirlineByAirlineName(airline.getName());
            if (airlineDB != null) {
                bindingResult.rejectValue("name", null, "Авиакомпания с таким названием уже существует");
            }
        }
        if (bindingResult.hasErrors()) return "airline-views/formUpdate";
        airlineService.createOrUpdateAirline(airline);
        return "redirect:/airlines/index";
    }

    @GetMapping(value = "/sortByName")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String sortByAirlineName(Model model) {
        List<Airline> airlines = airlineService.fetchAll();
        Collections.sort(airlines, Comparator.comparing(Airline::getName));
        model.addAttribute(LIST_AIRLINES, airlines);
        return "airline-views/airlines";
    }

    @GetMapping(value = "/sortByPayerName")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant', 'Finance department employee')")
    public String sortByPayerName(Model model) {
        List<Airline> airlines = airlineService.fetchAll();
        Collections.sort(airlines, Comparator.comparing(Airline::getPayerName));
        model.addAttribute(LIST_AIRLINES, airlines);
        return "airline-views/airlines";
    }
}
