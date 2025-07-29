package com.joelle.backend.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StaffResolver {
@Autowired
private PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;

    @Autowired
    public StaffResolver(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @QueryMapping
    public List<Staff> allStaff() {
        return staffRepository.findAll();
    }
@MutationMapping
public Boolean deleteStaff(@Argument Long id) {
    if (staffRepository.existsById(id)) {
        staffRepository.deleteById(id);
        return true;
    }
    return false;
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
    String encodedPassword = passwordEncoder.encode(password);
    Staff staff = new Staff(name, role, email, encodedPassword);
    return staffRepository.save(staff);
}
}

