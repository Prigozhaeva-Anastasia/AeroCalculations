package com.prigozhaeva.aerocalculations.dto;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.Flight;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceDTO {
    private Long id;
    private int invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String currency;
    private String paymentState;
    private Flight flight;
    private Employee employee;
    private double totalCostForGroundHandling;
    private double totalCostOfAirportServices;
    private double totalCost;
}
