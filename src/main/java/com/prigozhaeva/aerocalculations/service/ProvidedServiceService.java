package com.prigozhaeva.aerocalculations.service;

import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import java.util.List;

public interface ProvidedServiceService {
    List<ProvidedService> findProvidedServicesByFlightNumber(String flightNumber);
    void importProvidedServices(String path);
    ProvidedService createProvidedService(Long serviceId, String flightNumber);
    ProvidedService findProvidedServiceById(Long id);
    ProvidedService createOrUpdateProvidedService(Long providedServiceId, short amount);
}
