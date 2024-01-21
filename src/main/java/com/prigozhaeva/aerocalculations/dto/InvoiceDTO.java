package com.prigozhaeva.aerocalculations.dto;

import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Data
public class InvoiceDTO {
    private int invoiceNumber;
    private LocalDate invoiceCreationDate;
    private String currency;
    private String paymentState;
    private Flight flight;
    private Employee employee;
    private BigDecimal totalCostOfAirportServ;
    private BigDecimal totalCostOfGroundHandlingServ;
    private BigDecimal totalCost;
    private BigDecimal discount;
    private List<ProvidedService> airportServices;
    private List<ProvidedService> groundHandlingServices;
    public void setTotalCostOfAirportServ(BigDecimal totalCostOfAirportServ) {
        this.totalCostOfAirportServ = totalCostOfAirportServ.setScale(2, RoundingMode.HALF_UP);
    }
    public void setTotalCostOfGroundHandlingServ(BigDecimal totalCostOfGroundHandlingServ) {
        this.totalCostOfGroundHandlingServ = totalCostOfGroundHandlingServ.setScale(2, RoundingMode.HALF_UP);
    }
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost.setScale(2, RoundingMode.HALF_UP);
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount.setScale(2, RoundingMode.HALF_UP);
    }
}
