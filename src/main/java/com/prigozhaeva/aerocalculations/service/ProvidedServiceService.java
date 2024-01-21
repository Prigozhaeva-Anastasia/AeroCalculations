package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.ProvidedService;

import java.time.LocalDate;
import java.util.List;

public interface ProvidedServiceService {
    List<ProvidedService> findProvidedServicesByFlightId(Long flightId);
    void importProvidedServices(String path);
    ProvidedService createProvidedService(Long serviceId, Long flightId);
    ProvidedService findProvidedServiceById(Long id);
    ProvidedService createOrUpdateProvidedService(Long providedServiceId, short amount);
    List<ProvidedService> updateProvidedServices(List<ProvidedService> providedServices, Long flightId);
    void countValueOfProvidedService(ProvidedService providedService, String currency, LocalDate creationDate);
}
