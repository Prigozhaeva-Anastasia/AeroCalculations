package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.entity.Service;
import com.prigozhaeva.aerocalculations.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/services")
public class ServiceController {
    private ServiceService serviceService;

    public ServiceController(ServiceService service) {
        this.serviceService = service;
    }

    @GetMapping(value = "/index")
    public String services(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Service> services = new CopyOnWriteArrayList<>(serviceService.findServicesByServiceName(keyword));
        model.addAttribute(LIST_SERVICES, services);
        model.addAttribute(KEYWORD, keyword);
        return "service-views/services";
    }

    @GetMapping(value = "/formUpdate")
    public String updateService(Model model, Long serviceId) {
        Service service = serviceService.findServiceById(serviceId);
        model.addAttribute(SERVICE, service);
        return "service-views/formUpdate";
    }

    @PostMapping(value = "/update")
    public String update(@Valid Service service, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "service-views/formUpdate";
        }
        serviceService.createOrUpdateService(service);
        return "redirect:/services/index";
    }
}
