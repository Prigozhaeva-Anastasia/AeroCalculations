package com.prigozhaeva.aerocalculations.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ReportService {
    Map<Integer, Long> flightDynamics(int year, int month);
    Map<String, Long> airportInfrastructureDynamics(String date);
    Map<Integer, Long> flightDynamicsOnWeekDay(int year, int month, int weekDay);
    Map<Integer, BigDecimal> chartOfFeesChargedByAirlines(int year);
}
