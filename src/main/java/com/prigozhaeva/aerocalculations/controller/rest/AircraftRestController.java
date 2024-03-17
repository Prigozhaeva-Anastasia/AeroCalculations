package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.service.AircraftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aircrafts/")
public class AircraftRestController {
    private AircraftService aircraftService;

    public AircraftRestController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @PostMapping(value = "/import")
    @PreAuthorize("hasAuthority('Admin')")
    public void importAircrafts(@RequestParam String path) {
        aircraftService.importAircrafts(path);
    }
}
