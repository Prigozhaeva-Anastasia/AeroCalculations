package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Service;
import com.prigozhaeva.aerocalculations.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/services")
public class ServiceController {
    private ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    @GetMapping(value = "/index")
    public String services(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Service> services = new CopyOnWriteArrayList<>(service.findServicesByServiceName(keyword));
        model.addAttribute(LIST_SERVICES, services);
        model.addAttribute(KEYWORD, keyword);
        return "service-views/services";
    }
}
