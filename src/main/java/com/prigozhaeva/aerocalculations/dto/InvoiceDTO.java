package com.prigozhaeva.aerocalculations.dto;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.Flight;

import java.time.LocalDate;

public class InvoiceDTO {
    private int invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String currency;
    private Flight flight;
    private Employee employee;
    private double totalCostForGroundHandling;
    private double totalCostOfAirportServices;
    private double totalCost;
}
