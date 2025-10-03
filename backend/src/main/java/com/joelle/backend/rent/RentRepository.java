package com.joelle.backend.rent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Long> {
    Optional<Rent> findByStaffIdAndMonth(Long staffId, String month);
        boolean existsByStaffIdAndMonth(Long staffId, String month);

    @Query("SELECT s.id FROM Staff s")
    List<Long> findAllStaffIds();


    List<Rent> findByStaffId(Long staffId);
}
