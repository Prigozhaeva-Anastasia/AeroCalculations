package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.dto.InvoiceCreateDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceUpdateDTO;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.service.ProvidedServiceService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@RestController
@RequestMapping("/api/invoices/")
public class InvoiceRestController {
    private InvoiceService invoiceService;
    private ProvidedServiceService providedServiceService;
    private MappingUtils mappingUtils;

    public InvoiceRestController(InvoiceService invoiceService, ProvidedServiceService providedServiceService, MappingUtils mappingUtils) {
        this.invoiceService = invoiceService;
        this.providedServiceService = providedServiceService;
        this.mappingUtils = mappingUtils;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant')")
    public void createInvoice(@RequestBody InvoiceCreateDTO invoiceCreateDTO, Principal principal) {
        invoiceCreateDTO.setAirportServices(providedServiceService.updateProvidedServices(invoiceCreateDTO.getAirportServices(), invoiceCreateDTO.getFlightId()));
        invoiceCreateDTO.setGroundHandlingServices(providedServiceService.updateProvidedServices(invoiceCreateDTO.getGroundHandlingServices(), invoiceCreateDTO.getFlightId()));
        Invoice invoice = mappingUtils.mapToInvoice(invoiceCreateDTO, principal.getName());
        updateProvidedServices(invoiceCreateDTO.getAirportServices(), invoice);
        updateProvidedServices(invoiceCreateDTO.getGroundHandlingServices(), invoice);
        invoiceService.createInvoice(invoiceCreateDTO, principal.getName());
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAnyAuthority('Admin', 'Accountant')")
    public void updateInvoice(@RequestBody InvoiceUpdateDTO invoiceDTO) {
        Invoice invoiceDB = invoiceService.findInvoiceById(invoiceDTO.getId());
        invoiceDTO.setAirportServices(providedServiceService.updateProvidedServices(invoiceDTO.getAirportServices(), invoiceDTO.getId()));
        invoiceDTO.setGroundHandlingServices(providedServiceService.updateProvidedServices(invoiceDTO.getGroundHandlingServices(), invoiceDTO.getId()));
        Invoice invoice =  mappingUtils.mapToInvoice(invoiceDTO);
        updateProvidedServices(invoiceDTO.getAirportServices(),invoice);
        updateProvidedServices(invoiceDTO.getGroundHandlingServices(), invoice);
        invoice.setPaymentState(invoiceDB.getPaymentState());
        invoice.setFlight(invoiceDB.getFlight());
        invoice.setEmployee(invoiceDB.getEmployee());
        invoiceService.createOrUpdateInvoice(invoice);
    }
    private void updateProvidedServices(List<ProvidedService> providedServices, Invoice invoice) {
        providedServices.forEach(providedService -> {
            if (providedService.getAmount() == 0 && providedService.getService().getName().contains(PASSENGER) && !providedService.getService().getName().contains(DISABLED_PERSON) && !providedService.getService().getName().contains(CHILDREN)) {
                providedService.setAmount(providedService.getFlight().getNumOfAdults());
            } else if (providedService.getAmount() == 0 && providedService.getService().getName().contains(PASSENGER) && !providedService.getService().getName().contains(DISABLED_PERSON) && providedService.getService().getName().contains(CHILDREN)) {
                providedService.setAmount((short) (providedService.getFlight().getNumOfChildren() + providedService.getFlight().getNumOfBabies()));
            }
            providedServiceService.countValueOfProvidedService(providedService, invoice.getCurrency(), invoice.getInvoiceCreationDate());
        });
    }
}
