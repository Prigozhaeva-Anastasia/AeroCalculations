package com.prigozhaeva.aerocalculations.util;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceCreateDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import com.prigozhaeva.aerocalculations.service.EmployeeService;
import com.prigozhaeva.aerocalculations.service.FlightService;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public Invoice mapToInvoice(InvoiceCreateDTO dto){
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setInvoiceCreationDate(dto.getInvoiceCreationDate());
        invoice.setCurrency(dto.getCurrency());
        invoice.setPaymentState(NOT_PAID_STATUS);
        invoice.setFlight(flightService.findFlightById(dto.getFlightId()));
        invoice.setEmployee(employeeService.findEmployeeByEmail("astapovich@gmail.com"));//change_this
        return invoice;
    }
}
