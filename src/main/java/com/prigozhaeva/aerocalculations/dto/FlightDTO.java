package com.prigozhaeva.aerocalculations.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private String flightDirection;
    private String flightType;
    private String depCity;
    private String arrCity;
    private LocalDate depDate;
    private LocalDate arrDate;
    private LocalTime depTime;
    private LocalTime arrTime;
    private int luggageWeight;
    private int numOfAdults;
    private int numOfChildren;
    private int numOfBabies;
    private String tailNumber;
    private String airlineName;
}
