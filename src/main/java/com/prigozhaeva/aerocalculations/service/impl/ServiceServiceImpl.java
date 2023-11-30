package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.repository.ServiceRepository;
import com.prigozhaeva.aerocalculations.service.ServiceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServiceServiceImpl implements ServiceService {
    private ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<com.prigozhaeva.aerocalculations.entity.Service> findServicesByServiceName(String serviceName) {
        return serviceRepository.findServicesByNameContainsIgnoreCase(serviceName);
    }
}
