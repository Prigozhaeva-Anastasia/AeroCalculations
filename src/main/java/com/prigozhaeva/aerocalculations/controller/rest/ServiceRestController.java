package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.service.ServiceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services/")
public class ServiceRestController {
    private ServiceService serviceService;

    public ServiceRestController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    @PostMapping(value = "/import")
    public void importServices(@RequestParam String path) {
        serviceService.importServices(path);
    }
}
