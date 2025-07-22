package com.joelle.backend.clients;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joelle.backend.staff.Staff;

public interface ClientsRepository extends JpaRepository<Clients, Long>{

    

}
