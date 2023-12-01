package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.service.ProvidedServiceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/providedServices/")
public class ProvidedServiceRestController {
    private ProvidedServiceService providedServiceService;

    public ProvidedServiceRestController(ProvidedServiceService providedServiceService) {
        this.providedServiceService = providedServiceService;
    }

    @PostMapping(value = "/import")
    public void importProvidedServices(@RequestParam String path) {
        providedServiceService.importProvidedServices(path);
    }
}
