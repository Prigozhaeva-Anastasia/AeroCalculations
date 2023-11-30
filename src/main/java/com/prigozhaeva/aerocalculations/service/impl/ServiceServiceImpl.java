package com.prigozhaeva.aerocalculations.service.impl;

import com.prigozhaeva.aerocalculations.repository.ServiceRepository;
import com.prigozhaeva.aerocalculations.service.ServiceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.prigozhaeva.aerocalculations.constant.Constant.*;

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

    @Override
    public void importServices(String path) {
        List<com.prigozhaeva.aerocalculations.entity.Service> services = parse(path);
        if (services != null && services.size() != 0) {
            serviceRepository.saveAll(services);
        }
    }

    public List<com.prigozhaeva.aerocalculations.entity.Service> parse(String path) {
        List<com.prigozhaeva.aerocalculations.entity.Service> services;
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(new FileInputStream(path));
            services = new ArrayList<>();
            com.prigozhaeva.aerocalculations.entity.Service service = new com.prigozhaeva.aerocalculations.entity.Service();
            while (reader.hasNext()) {
                reader.next();
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    switch (reader.getName().getLocalPart()) {
                        case "service":
                            service = new com.prigozhaeva.aerocalculations.entity.Service();
                            break;
                        case "id":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                service.setId(Long.valueOf(reader.getText()));
                            }
                        case "name":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                service.setName(reader.getText());
                            }
                            break;
                        case "service_type":
                            reader.next();
                            if (reader.getEventType() == XMLEvent.CHARACTERS) {
                                String serviceType = reader.getText();
                                if (serviceType.equals(AIRPORT_SERVICES_SHORT)) {
                                    service.setServiceType(AIRPORT_SERVICES);
                                } else {
                                    service.setServiceType(GROUND_HANDLING_SERVICES);
                                }
                            }
                            break;
                    }
                }
                if (reader.getEventType() == XMLEvent.END_ELEMENT) {
                    services.add(service);
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return services;
    }
}
