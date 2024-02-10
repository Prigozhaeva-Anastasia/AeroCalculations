package com.prigozhaeva.aerocalculations.service.impl;

import java.io.FileOutputStream;
import java.security.cert.Certificate;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import com.prigozhaeva.aerocalculations.dto.InvoiceCreateDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceUpdateDTO;
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
import java.util.Enumeration;
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
    public List<InvoiceDTO> findInvoicesDtoByInvoiceNumber(int invoiceNumber) {
        return invoiceRepository.findInvoicesByInvoiceNumber(invoiceNumber).stream()
                .map(mappingUtils::mapToInvoiceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO findInvoiceDtoByInvoiceNumber(int invoiceNumber) {
        return mappingUtils.mapToInvoiceDTO(invoiceRepository.findInvoiceByInvoiceNumber(invoiceNumber));
    }

    @Override
    public InvoiceUpdateDTO findInvoiceUpdateDtoByInvoiceNumber(int invoiceNumber) {
        return mappingUtils.mapToInvoiceUpdateDTO(invoiceRepository.findInvoiceByInvoiceNumber(invoiceNumber));
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
        if (flightId != null)
            flight = flightRepository.findById(flightId).orElseThrow(() -> new EntityNotFoundException("Flight with id " + flightId + " Not Found"));//delete
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

    @Override
    public void removeInvoice(Long invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    @Override
    public void signDocument(String file) {
        try {
            String SRC_PDF = "D:/diploma/проект/pdf/" + file;
            String KEYSTORE_PATH = "C:/Users/user/Folder/private-key.p12";
            char[] PASSWORD = "1023712".toCharArray();
            String DEST_PDF = "D:/diploma/проект/pdf/signed/" + file;
            Rectangle rectangle = new Rectangle(66, 148, 150, 180);
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(KEYSTORE_PATH), PASSWORD);
            String alias = null;
            Enumeration<String> aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
                if (ks.isKeyEntry(alias))
                    break;
            }
            PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
            Certificate[] chain = ks.getCertificateChain(alias);
            PdfReader reader = new PdfReader(SRC_PDF);
            FileOutputStream os = new FileOutputStream(DEST_PDF);
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
            PdfContentByte canvas = stamper.getOverContent(1);
            canvas.rectangle(rectangle.getLeft(), rectangle.getBottom(), rectangle.getWidth(), rectangle.getHeight());
            canvas.stroke();
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setReason("for payment");
            appearance.setLocation("Minsk");
            appearance.setVisibleSignature(rectangle, 1, "Signature");
            ExternalDigest digest = new BouncyCastleDigest();
            ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, null);
            MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


