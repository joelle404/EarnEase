package com.joelle.backend.security;

import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class AuthResolver {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StaffRepository staffRepository;

    @MutationMapping
    public LoginResponse login(@Argument String email, @Argument String password) {
        String normalizedEmail = email.trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, password)
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(normalizedEmail);
        String token = jwtUtil.generateToken(userDetails);
        Staff staff = staffRepository.findByEmail(normalizedEmail)
            .orElseThrow(() -> new RuntimeException("Staff not found with email: " + normalizedEmail));

        return new LoginResponse(token, staff);
    }
}
