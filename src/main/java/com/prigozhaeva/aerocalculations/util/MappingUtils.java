package com.prigozhaeva.aerocalculations.util;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.dto.InvoiceDTO;
import com.prigozhaeva.aerocalculations.entity.Flight;
import com.prigozhaeva.aerocalculations.entity.Invoice;
import org.springframework.stereotype.Service;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Service
public class MappingUtils {
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
        return dto;
    }
}
