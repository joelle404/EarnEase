package com.joelle.backend.StaffService;

import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;
import com.joelle.backend.service.Service;
import com.joelle.backend.service.ServiceRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class StaffServiceResolver {

    private final StaffServiceRepository staffServiceRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;

    public StaffServiceResolver(StaffServiceRepository staffServiceRepository,
                                StaffRepository staffRepository,
                                ServiceRepository serviceRepository) {
        this.staffServiceRepository = staffServiceRepository;
        this.staffRepository = staffRepository;
        this.serviceRepository = serviceRepository;
    }
@PreAuthorize("isAuthenticated()")
    @MutationMapping
    public StaffService createStaffService(
            @Argument Long staffId,
            @Argument Long serviceId,
            @Argument BigDecimal customPrice
    ) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id " + staffId));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id " + serviceId));

        StaffService staffService = new StaffService();
        staffService.setStaffId(staff.getId());
        staffService.setServiceId(service.getId());
        staffService.setCustomPrice(customPrice);

        return staffServiceRepository.save(staffService);
    }
@PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Service> getServicesByStaffId(@Argument Long staffId) {
        return staffServiceRepository.findAllByStaffId(staffId).stream()
                .map(ss -> serviceRepository.findById(ss.getServiceId())
                        .orElseThrow(() -> new RuntimeException("Service not found with id " + ss.getServiceId()))
                )
                .collect(Collectors.toList());
    }
}