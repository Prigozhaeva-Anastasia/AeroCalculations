package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.entity.Service;
import com.prigozhaeva.aerocalculations.service.FlightService;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/invoices")
public class InvoiceController {
    private InvoiceService invoiceService;
    private FlightService flightService;
    private ServiceService serviceService;

    public InvoiceController(InvoiceService invoiceService, FlightService flightService, ServiceService serviceService) {
        this.invoiceService = invoiceService;
        this.flightService = flightService;
        this.serviceService = serviceService;
    }

    @GetMapping(value = "/index")
    public String invoices(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<InvoiceDTO> invoiceList;
        if (keyword.isEmpty()) {
            invoiceList = new CopyOnWriteArrayList<>(invoiceService.fetchAllDto());
        } else {
            invoiceList = new CopyOnWriteArrayList<>(invoiceService.findInvoiceDtoByInvoiceNumber(keyword));
        }
        model.addAttribute(LIST_INVOICES, invoiceList);
        model.addAttribute(KEYWORD, keyword);
        return "invoice-views/invoices";
    }

    @PostMapping(value = "/changePaymentStatus")
    public String changePaymentStatus(Long invoiceId, String paymentState) {
        //invoiceService.changePaymentStatus(invoiceId, paymentState);
        return "redirect:/invoices/index";
    }

    @GetMapping(value = "/formCreate")
    public String formCreate(Model model, String flightNumberModal, String invoiceCreationDate) {
        Flight flight = flightService.findFlightByFlightNumberAndDepDate(flightNumberModal, LocalDate.parse(invoiceCreationDate));
        List<ProvidedService> airportServicesCheck = flight.getProvidedServices().stream()
                        .filter(providedService->providedService.getService().getServiceType().equals(AIRPORT_SERVICES))
                        .collect(Collectors.toList());
        List<ProvidedService> groundHandlingServicesCheck = flight.getProvidedServices().stream()
                .filter(providedService->providedService.getService().getServiceType().equals(GROUND_HANDLING_SERVICES))
                .collect(Collectors.toList());
        List<Service> airportServices = serviceService.fetchAll().stream()
                .filter(service1 -> airportServicesCheck.stream()
                        .anyMatch(service2 -> service1.getId() != service2.getService().getId() && service1.getServiceType().equals(service2.getService().getServiceType())))
                .collect(Collectors.toList());
        List<Service> groundHandlingServices = serviceService.fetchAll().stream()
                .filter(service1 -> groundHandlingServicesCheck.stream()
                        .anyMatch(service2 -> service1.getId() != service2.getService().getId() && service1.getServiceType().equals(service2.getService().getServiceType())))
                .collect(Collectors.toList());
        Optional<Integer> number = invoiceService.fetchAll().stream()
                .map(Invoice::getInvoiceNumber)
                .max(Integer::compareTo);
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute(FLIGHT, flight);
        model.addAttribute(AIRPORT_SERVICES_MODEL_CHECK, airportServicesCheck);
        model.addAttribute(GROUND_HANDLING_SERVICES_MODEL_CHECK, groundHandlingServicesCheck);
        model.addAttribute(AIRPORT_SERVICES_MODEL, airportServices);
        model.addAttribute(GROUND_HANDLING_SERVICES_MODEL, groundHandlingServices);
        model.addAttribute(INVOICE, Invoice.builder()
                .invoiceCreationDate(LocalDate.parse(currentDate))
                .invoiceNumber(number.get().intValue()+1)
                .build()
        );
        return "invoice-views/formCreate";
    }
}
