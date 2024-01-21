package com.prigozhaeva.aerocalculations.controller;

import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.*;
import com.prigozhaeva.aerocalculations.service.FlightService;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.service.ServiceService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Controller
@RequestMapping(value = "/invoices")
public class InvoiceController {
    private InvoiceService invoiceService;
    private FlightService flightService;
    private ServiceService serviceService;
    private MappingUtils mappingUtils;

    public InvoiceController(InvoiceService invoiceService, FlightService flightService, ServiceService serviceService, MappingUtils mappingUtils) {
        this.invoiceService = invoiceService;
        this.flightService = flightService;
        this.serviceService = serviceService;
        this.mappingUtils = mappingUtils;
    }

    @GetMapping(value = "/index")
    public String invoices(Model model, @RequestParam(name = KEYWORD, defaultValue = "") String keyword) {
        List<Invoice> invoices = invoiceService.fetchAll();  //delete
        invoices.get(0).setFlight(flightService.findFlightById(2001399399L)); //delete
        invoices.get(1).setFlight(flightService.findFlightById(2001406321L)); //delete
        invoices.get(2).setFlight(flightService.findFlightById(2001406319L)); //delete
        for (Invoice invoice : invoices) {
            invoiceService.createOrUpdateInvoice(invoice);  //delete
        }
        List<InvoiceDTO> invoiceList;
        if (keyword.isEmpty()) {
            invoiceList = new CopyOnWriteArrayList<>(invoiceService.fetchAllDto());
        } else {
            invoiceList = new CopyOnWriteArrayList<>(invoiceService.findInvoiceDtoByInvoiceNumber(Integer.parseInt(keyword)));
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
        if (flight != null) {
            List<ProvidedService> airportServicesCheck = flight.getProvidedServices().stream()
                    .filter(providedService -> providedService.getService().getServiceType().equals(AIRPORT_SERVICES))
                    .collect(Collectors.toList());
            List<ProvidedService> groundHandlingServicesCheck = flight.getProvidedServices().stream()
                    .filter(providedService -> providedService.getService().getServiceType().equals(GROUND_HANDLING_SERVICES))
                    .collect(Collectors.toList());
            Set<ProvidedService> airportServices = serviceService.fetchAll().stream()
                    .filter(service -> service.getServiceType().equals(AIRPORT_SERVICES))
                    .filter(service -> airportServicesCheck.stream()
                            .noneMatch(providedService -> providedService.getService().getId().equals(service.getId())))
                    .map(service -> ProvidedService.builder().service(service).build())
                    .collect(Collectors.toSet());
            List<ProvidedService> groundHandlingServices = serviceService.fetchAll().stream()
                    .filter(service -> service.getServiceType().equals(GROUND_HANDLING_SERVICES))
                    .filter(service -> groundHandlingServicesCheck.stream()
                            .noneMatch(providedService -> providedService.getService().getId().equals(service.getId())))
                    .map(service -> ProvidedService.builder().service(service).build())
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
                    .invoiceNumber(number.get().intValue() + 1)
                    .build()
            );
            return "invoice-views/formCreate";
        }
        else {
            return "invoice-views/msgPage";
        }
    }

    @GetMapping(value = "/confirmForm")
    public String confirmForm(int invoiceNumber, Model model) {
        Invoice invoiceDB = invoiceService.findInvoiceByInvoiceNumber(invoiceNumber);
        InvoiceDTO invoiceDTO = mappingUtils.mapToInvoiceDTO(invoiceDB);
        model.addAttribute(INVOICE, invoiceDTO);
        model.addAttribute(CURRENCY_SYMBOL, Currency.getInstance(invoiceDTO.getCurrency()).getSymbol());
        return "invoice-views/confirmForm";
    }
}
