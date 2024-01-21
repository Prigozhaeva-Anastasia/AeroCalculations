package com.prigozhaeva.aerocalculations.controller.rest;

import com.prigozhaeva.aerocalculations.dto.InvoiceCreateDTO;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.service.ProvidedServiceService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.web.bind.annotation.*;

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
    public void createInvoice(@RequestBody InvoiceCreateDTO invoiceCreateDTO) {
        invoiceCreateDTO.setAirportServices(providedServiceService.updateProvidedServices(invoiceCreateDTO.getAirportServices(), invoiceCreateDTO.getFlightId()));
        invoiceCreateDTO.setGroundHandlingServices(providedServiceService.updateProvidedServices(invoiceCreateDTO.getGroundHandlingServices(), invoiceCreateDTO.getFlightId()));
        updateProvidedServices(invoiceCreateDTO.getAirportServices(), invoiceCreateDTO);
        updateProvidedServices(invoiceCreateDTO.getGroundHandlingServices(), invoiceCreateDTO);
        invoiceService.createInvoice(invoiceCreateDTO);
    }
    private void updateProvidedServices(List<ProvidedService> providedServices, InvoiceCreateDTO invoiceCreateDTO) {
        providedServices.forEach(providedService -> {
            if (providedService.getAmount() == 0 && providedService.getService().getName().contains(PASSENGER) && !providedService.getService().getName().contains(DISABLED_PERSON) && !providedService.getService().getName().contains(CHILDREN)) {
                providedService.setAmount(providedService.getFlight().getNumOfAdults());
            } else if (providedService.getAmount() == 0 && providedService.getService().getName().contains(PASSENGER) && !providedService.getService().getName().contains(DISABLED_PERSON) && providedService.getService().getName().contains(CHILDREN)) {
                providedService.setAmount((short) (providedService.getFlight().getNumOfChildren() + providedService.getFlight().getNumOfBabies()));
            }
            providedServiceService.countValueOfProvidedService(providedService, invoiceCreateDTO.getCurrency(), invoiceCreateDTO.getInvoiceCreationDate());
        });
    }
}
