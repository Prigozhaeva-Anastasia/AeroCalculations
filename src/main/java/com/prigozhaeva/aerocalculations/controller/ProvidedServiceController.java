package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.entity.Service;
import com.prigozhaeva.aerocalculations.service.ProvidedServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
    public String providedServices(Model model, Long flightId) {
        List<ProvidedService> providedServices = providedServiceService.findProvidedServicesByFlightId(flightId);
        model.addAttribute(LIST_PROVIDED_SERVICES, providedServices);
        return "provided_service-views/providedServices";
    }

    @GetMapping(value = "/formUpdate")
    public String updateProvidedService(Model model, Long providedServiceId) {
        ProvidedService providedService = providedServiceService.findProvidedServiceById(providedServiceId);
        model.addAttribute(PROVIDED_SERVICE, providedService);
        return "provided_service-views/formUpdate";
    }

    @PostMapping(value = "/update")
    public String update(ProvidedService providedService) {
        ProvidedService result = providedServiceService.createOrUpdateProvidedService(providedService.getId(), providedService.getAmount());
        return "redirect:/providedServices/index?flightNumber=" + result.getFlight().getFlightNumber();
    }
}
