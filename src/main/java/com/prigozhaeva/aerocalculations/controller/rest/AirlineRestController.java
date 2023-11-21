package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.service.AirlineService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airlines/")
public class AirlineRestController {
    private AirlineService airlineService;

    public AirlineRestController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping(value = "/import")
    public void importAirlines(@RequestParam String path) {
        airlineService.importAirlines(path);
    }
}
