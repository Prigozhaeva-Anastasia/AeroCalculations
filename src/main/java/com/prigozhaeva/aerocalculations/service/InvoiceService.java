package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.dto.InvoiceCreateDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Invoice;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> findInvoiceDtoByInvoiceNumber(int invoiceNumber);
    List<InvoiceDTO> fetchAllDto();
    void changePaymentStatus(int invoiceNumber, String paymentState);
    Invoice findInvoiceById(Long id);
    List<Invoice> fetchAll();
    Invoice createInvoice(InvoiceCreateDTO invoiceCreateDTO);
    Invoice createInvoice(int invoiceNumber, LocalDate invoiceCreationDate, String currency, String paymentState, Long employeeId,Long flightId);
    Invoice findInvoiceByInvoiceNumber(int invoiceNumber);
    Invoice createOrUpdateInvoice(Invoice invoice);
}
