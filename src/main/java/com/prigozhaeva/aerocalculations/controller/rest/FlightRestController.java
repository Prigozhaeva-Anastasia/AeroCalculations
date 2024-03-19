package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.service.FlightService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flights/")
public class FlightRestController {
    private FlightService flightService;

    public FlightRestController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping(value = "/import")
    @PreAuthorize("hasAuthority('Admin')")
    public void importAirlines(@RequestParam String path) {
        flightService.importFlights(path);
    }
}
