package com.prigozhaeva.aerocalculations.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoicePaymentTermsDTO {
    private int invoiceNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String paymentMethod;
    private BigDecimal finesAndPenalties;
    private String currency;
    private String specialConditions;
    private String firstAndLastNames;
    private String phoneNumber;
}
