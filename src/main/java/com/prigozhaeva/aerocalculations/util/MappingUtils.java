package com.prigozhaeva.aerocalculations.util;

import com.prigozhaeva.aerocalculations.dto.FlightDTO;
import com.prigozhaeva.aerocalculations.entity.Flight;
import org.springframework.stereotype.Service;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

@Service
public class MappingUtils {
    public FlightDTO mapToFlightDTO(Flight flight){
        FlightDTO dto = new FlightDTO();
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
}
