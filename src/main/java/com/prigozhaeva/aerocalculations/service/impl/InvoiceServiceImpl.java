package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.dto.InvoiceCreateDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Employee;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.repository.EmployeeRepository;
import com.prigozhaeva.aerocalculations.repository.FlightRepository;
import com.prigozhaeva.aerocalculations.repository.InvoiceRepository;
import com.prigozhaeva.aerocalculations.service.InvoiceService;
import com.prigozhaeva.aerocalculations.util.MappingUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceRepository invoiceRepository;
    private EmployeeRepository employeeRepository;
    private FlightRepository flightRepository;
    private MappingUtils mappingUtils;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, EmployeeRepository employeeRepository, FlightRepository flightRepository, MappingUtils mappingUtils) {
        this.invoiceRepository = invoiceRepository;
        this.employeeRepository = employeeRepository;
        this.flightRepository = flightRepository;
        this.mappingUtils = mappingUtils;
    }

    @Override
    public List<InvoiceDTO> findInvoiceDtoByInvoiceNumber(int invoiceNumber) {
        return invoiceRepository.findInvoicesByInvoiceNumber(invoiceNumber).stream()
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
    public void changePaymentStatus(int invoiceNumber, String paymentState) {
        Invoice invoiceDB = invoiceRepository.findInvoiceByInvoiceNumber(invoiceNumber);
        invoiceDB.setPaymentState(paymentState);
        invoiceRepository.save(invoiceDB);
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

    @Override
    public Invoice createInvoice(InvoiceCreateDTO invoiceCreateDTO) {
        return invoiceRepository.save(mappingUtils.mapToInvoice(invoiceCreateDTO));
    }

    @Override
    public Invoice createInvoice(int invoiceNumber, LocalDate invoiceCreationDate, String currency, String paymentState, Long employeeId, Long flightId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " Not Found"));
        Flight flight;
        if (flightId != null) flight= flightRepository.findById(flightId).orElseThrow(() -> new EntityNotFoundException("Flight with id " + flightId + " Not Found"));//delete
        else flight = null;//delete
        return invoiceRepository.save(Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .invoiceCreationDate(invoiceCreationDate)
                .currency(currency)
                .paymentState(paymentState)
                .employee(employee)
                .flight(flight)
                .build());
    }

    @Override
    public Invoice findInvoiceByInvoiceNumber(int invoiceNumber) {
        return invoiceRepository.findInvoiceByInvoiceNumber(invoiceNumber);
    }
    public Invoice createOrUpdateInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

}
