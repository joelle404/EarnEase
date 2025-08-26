package com.joelle.backend.StaffService;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffServiceRepository extends JpaRepository<StaffService,Long>{
        List<StaffService> findAllByStaffId(Long staffId);

}
