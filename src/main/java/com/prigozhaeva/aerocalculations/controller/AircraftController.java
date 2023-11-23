package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Aircraft;
import com.prigozhaeva.aerocalculations.service.AircraftService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.KEYWORD;
import static com.prigozhaeva.aerocalculations.constant.Constant.LIST_AIRCRAFTS;

@Controller
@RequestMapping(value = "/aircrafts")
public class AircraftController {
    private AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }
    @GetMapping(value = "/index")
    public String aircrafts(Model model) {
        List<Aircraft> aircraftList = new CopyOnWriteArrayList<>(aircraftService.fetchAll());
        model.addAttribute(LIST_AIRCRAFTS, aircraftList);
        return "aircraft-views/aircrafts";
    }
}
