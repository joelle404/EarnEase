package com.joelle.backend.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StaffResolver {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffResolver(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @QueryMapping
    public List<Staff> allStaff() {
        return staffRepository.findAll();
    }

    @QueryMapping
    public Staff getStaffById(@Argument Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Staff createStaff(@Argument String name, 
                             @Argument Role role, 
                             @Argument String email, 
                             @Argument String password) {
        System.out.println("Received createStaff: " + name + ", " + role + ", " + email);
        Staff staff = new Staff(name, role, email, password);
        try {
            return staffRepository.save(staff);
        } catch (Exception e) {
            System.out.println("Error saving staff: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
