package com.prigozhaeva.aerocalculations.repository;

import com.prigozhaeva.aerocalculations.entity.ProvidedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProvideServiceRepository extends JpaRepository<ProvidedService, Long> {
    @Query(value = "select p from ProvidedService as p where p.flight.id=:flightId")
    List<ProvidedService> findProvidedServicesByFlightId(Long flightId);
}
