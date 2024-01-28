package com.prigozhaeva.aerocalculations.dto;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data

public class InvoiceUpdateDTO {
    private Long id;
    private int invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String currency;
    private List<ProvidedService> airportServices;
    private List<ProvidedService> groundHandlingServices;
}
