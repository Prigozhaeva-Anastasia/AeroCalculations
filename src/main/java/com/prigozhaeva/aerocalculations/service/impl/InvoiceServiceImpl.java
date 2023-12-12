package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.repository.InvoiceRepository;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceRepository invoiceRepository;
    private MappingUtils mappingUtils;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MappingUtils mappingUtils) {
        this.invoiceRepository = invoiceRepository;
        this.mappingUtils = mappingUtils;
    }

    @Override
    public List<InvoiceDTO> findInvoiceDtoByInvoiceNumber(int invoiceNumber) {
        return invoiceRepository.findInvoiceByInvoiceNumberContains(invoiceNumber).stream()
                .map(mappingUtils::mapToInvoiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> fetchAllDto() {
        return invoiceRepository.findAll().stream()
                .map(mappingUtils::mapToInvoiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void changePaymentStatus(Long invoiceId, String paymentState) {

    }

    @Override
    public Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice with id " + id + " Not Found"));
    }

    @Override
    public List<Invoice> fetchAll() {
        return invoiceRepository.findAll();
    }
}
