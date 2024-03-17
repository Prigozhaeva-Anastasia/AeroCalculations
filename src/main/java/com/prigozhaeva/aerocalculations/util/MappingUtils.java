package com.prigozhaeva.aerocalculations.util;

import com.prigozhaeva.aerocalculations.dto.*;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import com.prigozhaeva.aerocalculations.service.FlightService;
import lombok.Builder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Service
@Builder
public class  MappingUtils {
    private EmployeeService employeeService;
    private FlightService flightService;

    public MappingUtils(EmployeeService employeeService, FlightService flightService) {
        this.employeeService = employeeService;
        this.flightService = flightService;
    }

    public FlightDTO mapToFlightDTO(Flight flight){
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        if (flight.getFlightDirection().equals(DEPARTURE_SHORT)) {
            dto.setFlightDirection(DEPARTURE);
        } else {
            dto.setFlightDirection(ARRIVAL);
        }
        dto.setFlightType(flight.getFlightType());
        dto.setDepCity(CityCodeMap.getCityCodeMap().get(flight.getDepCity()));
        dto.setArrCity(CityCodeMap.getCityCodeMap().get(flight.getArrCity()));
        dto.setDepDate(flight.getDepDate());
        dto.setArrDate(flight.getArrDate());
        dto.setDepTime(flight.getDepTime());
        dto.setArrTime(flight.getArrTime());
        dto.setLuggageWeight(flight.getLuggageWeight());
        dto.setNumOfAdults(flight.getNumOfAdults());
        dto.setNumOfChildren(flight.getNumOfChildren());
        dto.setNumOfBabies(flight.getNumOfBabies());
        dto.setTailNumber(flight.getAircraft().getTailNumber());
        dto.setAirlineName(flight.getAircraft().getAirline().getName());
        return dto;
    }

    public InvoiceDTO mapToInvoiceDTO(Invoice invoice){
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setInvoiceCreationDate(invoice.getInvoiceCreationDate());
        dto.setCurrency(invoice.getCurrency());
        dto.setPaymentState(invoice.getPaymentState());
        dto.setFlight(invoice.getFlight());
        dto.setEmployee(invoice.getEmployee());
        dto.setAirportServices(invoice.getFlight().getProvidedServices().stream()
                .filter(providedService -> providedService.getService().getServiceType().equals(AIRPORT_SERVICES))
                .collect(Collectors.toList()));
        dto.setGroundHandlingServices(invoice.getFlight().getProvidedServices().stream()
                .filter(providedService -> providedService.getService().getServiceType().equals(GROUND_HANDLING_SERVICES))
                .collect(Collectors.toList()));
        dto.setTotalCost(countTotalCostIncludingDiscounts(dto));
        return dto;
    }

    private BigDecimal countTotalCostIncludingDiscounts(InvoiceDTO dto) {
        dto.setTotalCostOfAirportServ(dto.getAirportServices().stream()
                .filter(service -> service.getValue() != null)
                .map(ProvidedService::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setTotalCostOfGroundHandlingServ(dto.getGroundHandlingServices().stream()
                .filter(service -> service.getValue() != null)
                .map(ProvidedService::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal totalCost = dto.getTotalCostOfAirportServ().add(dto.getTotalCostOfGroundHandlingServ());
        if (checkingTheCompletedFlightPlan(dto)) {
            BigDecimal discount = totalCost.multiply(BigDecimal.valueOf(0.2));
            dto.setDiscount(discount);
            totalCost = totalCost.subtract(discount);
        }
        return totalCost;
    }
    private boolean checkingTheCompletedFlightPlan(InvoiceDTO dto) {
        LocalDate flightDate = dto.getFlight().getDepDate();
        Long airlineId = dto.getFlight().getAircraft().getAirline().getId();
        List<Flight> flights = flightService.findFlightsByMonthOfDepDateAndAirline(flightDate, airlineId);
        return flights.size() >= FLIGHT_PLAN;
    }

    public Invoice mapToInvoice(InvoiceCreateDTO dto, String employeeEmail){
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setInvoiceCreationDate(dto.getInvoiceCreationDate());
        invoice.setCurrency(dto.getCurrency());
        invoice.setPaymentState(NOT_PAID_STATUS);
        invoice.setFlight(flightService.findFlightById(dto.getFlightId()));
        invoice.setEmployee(employeeService.findEmployeeByEmail(employeeEmail));
        return invoice;
    }

    public Invoice mapToInvoice(InvoiceUpdateDTO dto){
        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setInvoiceCreationDate(dto.getInvoiceCreationDate());
        invoice.setCurrency(dto.getCurrency());
        return invoice;
    }

    public Invoice mapToInvoice(InvoiceDTO dto, String employeeEmail){
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setInvoiceCreationDate(dto.getInvoiceCreationDate());
        invoice.setCurrency(dto.getCurrency());
        invoice.setPaymentState(NOT_PAID_STATUS);
        invoice.setFlight(dto.getFlight());
        invoice.setEmployee(employeeService.findEmployeeByEmail(employeeEmail));
        return invoice;
    }

    public InvoiceUpdateDTO mapToInvoiceUpdateDTO(Invoice invoice){
        InvoiceUpdateDTO dto = new InvoiceUpdateDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setInvoiceCreationDate(invoice.getInvoiceCreationDate());
        dto.setCurrency(invoice.getCurrency());
        dto.setAirportServices(invoice.getFlight().getProvidedServices().stream()
                .filter(providedService -> providedService.getService().getServiceType().equals(AIRPORT_SERVICES))
                .collect(Collectors.toList()));
        dto.setGroundHandlingServices(invoice.getFlight().getProvidedServices().stream()
                .filter(providedService -> providedService.getService().getServiceType().equals(GROUND_HANDLING_SERVICES))
                .collect(Collectors.toList()));
        return dto;
    }

    public MessageDTO mapToMessageDTO(Message message) throws MessagingException, IOException {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageId(message.getHeader("Message-ID")[0]);
        messageDTO.setSenderEmail(((InternetAddress) message.getFrom()[0]).getAddress());
        messageDTO.setSubject(message.getSubject());
        Date date = message.getReceivedDate();
        Instant instant = date.toInstant();
        messageDTO.setLocalDateTime(instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
        StringBuilder textBuilder = new StringBuilder();
        processMessageContent(message, textBuilder);
        messageDTO.setText(textBuilder.toString());
        return messageDTO;
    }

    public MessageDTO mapToMessageDTOWithAttachment(Message message) throws MessagingException, IOException {
        MessageDTO messageDTO = mapToMessageDTO(message);
        if (message.getContent() instanceof Multipart) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
                        bodyPart.getContentType().toLowerCase().contains("application/pdf")) {
                    String fileName = bodyPart.getFileName();
                    String filePath = PATH_TO_DOWNLOADED_FILES + fileName;
                    messageDTO.setFilePath(filePath);
                    try (InputStream inputStream = bodyPart.getInputStream();
                         FileOutputStream outputStream = new FileOutputStream(filePath)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        }
        return messageDTO;
    }

    private void processMessageContent(Part part, StringBuilder textBuilder) throws MessagingException, IOException {
        if (part.isMimeType("text/plain")) {
            String text = part.getContent().toString();
            int endIndex = text.indexOf("--");
            if (endIndex != -1) {
                textBuilder.append(text.substring(0, endIndex));
            } else {
                textBuilder.append(text);
            }
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                processMessageContent(bodyPart, textBuilder);
            }
        }
    }
}
