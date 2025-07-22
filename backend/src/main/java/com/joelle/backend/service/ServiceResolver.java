package com.joelle.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ServiceResolver {
    
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceResolver(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @QueryMapping
    public List<Service> allServices() {
        return serviceRepository.findAll();
    }

    @QueryMapping
    public Service getServiceById(@Argument Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Service createService(@Argument String name,
                                 @Argument Double basePrice) {
        System.out.println("Received createService: " + name + ", " + basePrice);
        Service service = new Service(name, basePrice);
        try {
            return serviceRepository.save(service);
        } catch (Exception e) {
            System.out.println("Error saving service: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
