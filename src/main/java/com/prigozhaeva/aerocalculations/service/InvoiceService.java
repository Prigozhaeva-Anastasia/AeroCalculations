package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Invoice;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> findInvoiceDtoByInvoiceNumber(int invoiceNumber);
    List<InvoiceDTO> fetchAllDto();
    void changePaymentStatus(Long invoiceId, String paymentState);
    Invoice findInvoiceById(Long id);
}
