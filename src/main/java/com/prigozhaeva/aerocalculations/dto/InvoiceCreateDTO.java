package com.prigozhaeva.aerocalculations.dto;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceCreateDTO {
    private int invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String currency;
    private Long flightId;
    private List<ProvidedService> airportServices;
    private List<ProvidedService> groundHandlingServices;
}
