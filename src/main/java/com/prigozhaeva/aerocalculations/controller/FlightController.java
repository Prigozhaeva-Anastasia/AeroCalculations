package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/flights")
public class FlightController {
    private FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping(value = "/index")
    public String flights(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<FlightDTO> flightList = new CopyOnWriteArrayList<>(flightService.findFlightsByFlightNumber(keyword));
        model.addAttribute(LIST_FLIGHTS, flightList);
        model.addAttribute(KEYWORD, keyword);
        return "flight-views/flights";
    }
}
