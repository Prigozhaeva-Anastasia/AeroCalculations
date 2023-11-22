package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Airline;
import com.prigozhaeva.aerocalculations.service.AirlineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.KEYWORD;
import static com.prigozhaeva.aerocalculations.constant.Constant.LIST_AIRLINES;


@Controller
@RequestMapping(value = "/airlines")
public class AirlineController {
    private AirlineService airlineService;
    public AirlineController(AirlineService airlineService) {this.airlineService = airlineService;}

    @GetMapping(value = "/index")
    public String airlines(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Airline> airlineList = new CopyOnWriteArrayList<>(airlineService.findAirlinesByAirlineName(keyword));
        model.addAttribute(LIST_AIRLINES, airlineList);
        model.addAttribute(KEYWORD, keyword);
        return "airline-views/airlines";
    }
}
