package com.joelle.backend.rent;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

@Controller
public class RentResolver {
    
    private final RentRepository rentRepository;
    private final StaffRepository staffRepository;

    @Autowired
    public RentResolver(RentRepository rentRepository, StaffRepository staffRepository) {
        this.rentRepository = rentRepository;
        this.staffRepository = staffRepository;
    }

    @QueryMapping
    public List<Rent> allRents() {
        return rentRepository.findAll();
    }

    @QueryMapping
    public Rent getRentById(@Argument Long id) {
        return rentRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Rent createRent(@Argument Long staffId,
                           @Argument String month,
                           @Argument Double amount,
                           @Argument LocalDateTime paidDate) {
        Staff staff = staffRepository.findById(staffId).orElse(null);
        if (staff == null) {
            System.out.println("Invalid staff ID: " + staffId);
            return null;
        }

        Rent rent = new Rent(staff, month, amount, paidDate);
        try {
            return rentRepository.save(rent);
        } catch (Exception e) {
            System.out.println("Error saving rent: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
