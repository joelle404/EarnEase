package com.joelle.backend.security;

import com.joelle.backend.staff.Staff;
import com.joelle.backend.staff.StaffRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StaffRepository staffRepository;

 @Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Staff staff = staffRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

    return new User(
        staff.getEmail(),
        staff.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_" + staff.getRole().name()))
    );
}

}