package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.service.ProvidedServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/providedServices")
public class ProvidedServiceController {
    private ProvidedServiceService providedServiceService;

    public ProvidedServiceController(ProvidedServiceService providedServiceService) {
        this.providedServiceService = providedServiceService;
    }

    @GetMapping(value = "/index")
    public String providedServices(Model model, String flightNumber) {
        List<ProvidedService> providedServices = providedServiceService.findProvidedServicesByFlightNumber(flightNumber);
        model.addAttribute(LIST_PROVIDED_SERVICES, providedServices);
        return "provided_service-views/providedServices";
    }
}
